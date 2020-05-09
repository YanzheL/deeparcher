package com.hitnslab.dnssecurity.deeparcher.stream.processor

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainAssocDetailProto.DomainAssocDetail
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate
import com.hitnslab.dnssecurity.deeparcher.util.diffSize
import com.hitnslab.dnssecurity.deeparcher.util.intersectionSize
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import mu.KotlinLogging
import org.apache.kafka.streams.kstream.ValueMapper
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class GraphEdgeGenerator private constructor() : ValueMapper<DomainAssocDetail, Iterable<GraphAssocEdgeUpdate>> {

    companion object {
        val INSTANCE: GraphEdgeGenerator by lazy { GraphEdgeGenerator() }
    }

    val ipv4Table = mutableMapOf<String, ByteArray>()

    val cacheSize: Long = 100000L

    val ipv4Cache = Caffeine.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .maximumSize(cacheSize)
        .build<ByteBuf, MutableMap<String, Int>>()

    val ipv6Table = mutableMapOf<String, ByteArray>()

    val ipv6Cache = Caffeine.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .maximumSize(cacheSize)
        .build<ByteBuf, MutableMap<String, Int>>()

    val cnameTable = mutableMapOf<String, Collection<String>>()

//    val cnameCache = Caffeine.newBuilder()
//        .maximumSize(cacheSize)
//        .build<String, Collection<String>>()

    private val logger = KotlinLogging.logger {}

    override fun apply(value: DomainAssocDetail): Iterable<GraphAssocEdgeUpdate> {
        val result = mutableMapOf<String, Int>()
        val ipv4Bytes = value.ipv4Addrs.toByteArray()
        val ipv6Bytes = value.ipv6Addrs.toByteArray()
        selectBytesIntersectedNeighbors(value.fqdn, ipv4Bytes, 4, ipv4Table, ipv4Cache)
            .forEach { (k, v) -> result.merge(k, v) { v1, v2 -> v1 + v2 } }
        selectBytesIntersectedNeighbors(value.fqdn, ipv6Bytes, 16, ipv6Table, ipv6Cache)
            .forEach { (k, v) -> result.merge(k, v) { v1, v2 -> v1 + v2 } }
        selectStringsIntersectedNeighbors(value.fqdn, value.cnamesList, cnameTable)
            .forEach { (k, v) -> result.merge(k, v) { v1, v2 -> v1 + v2 } }
        return result.map { (k, v) ->
            // Make sure the natural order of fqdn1 < fqdn2
            val (first, last) = if (value.fqdn < k) value.fqdn to k else k to value.fqdn
            GraphAssocEdgeUpdate
                .newBuilder()
                .setFqdn1(first)
                .setFqdn2(last)
                .setNSharedFields(v)
                .build()
        }
    }

    /**
     * This function is similar to the following SQL statement.
     *
     * SELECT [table].key, [data].intersectionSize([table].value) AS ct
     * FROM [table]
     * WHERE [table].key = [key] AND ct > 0
     *
     * @param [key] primary key.
     * @param [data] new data.
     * @param [table] the table to scan.
     * @return the select result.
     */
    private fun selectStringsIntersectedNeighbors(
        key: String,
        data: Collection<String>,
        table: MutableMap<String, Collection<String>>
    ): Map<String, Int> {
        if (data.isEmpty()) {
            return emptyMap()
        }
        val result = ConcurrentHashMap<String, Int>()
        table[key] = data
        table.entries.parallelStream().forEach { (k, v) ->
            if (k != key) {
                val count = v.intersectionSize(data)
                if (count > 0) {
                    result[k] = count
                }
            }
        }
        return result
    }

    /**
     * This function is similar to the following SQL statement.
     *
     * SELECT [table].key, [data].intersectionSize([table].value, [wsize]) AS ct
     * FROM [table]
     * WHERE [table].key = [key] AND ct > 0
     *
     * @param [key] primary key.
     * @param [data] new data.
     * @param [wsize] size of comparison window.
     * @param [table] the table to scan.
     * @param [cache] cached recent results. The cache key should be a `ByteBuf` instance which contains one backing array.
     * @return the select result.
     */
    private fun selectBytesIntersectedNeighbors(
        key: String,
        data: ByteArray,
        wsize: Int,
        table: MutableMap<String, ByteArray>,
        cache: Cache<ByteBuf, MutableMap<String, Int>>
    ): Map<String, Int> {
        if (data.isEmpty()) {
            return emptyMap()
        }
        val dataBuf = Unpooled.wrappedUnmodifiableBuffer(Unpooled.wrappedBuffer(data))
        // Check old data in table.
        val old = table[key]
        // Use cache only if no old data in table or old data is same as current data.
        if (old == null || data.diffSize(old, wsize) == 0) {
            if (old == null) {
                // Put current data in table.
                table[key] = data
            }
            val cached = cache.getIfPresent(dataBuf)
            if (cached != null) {
                return cached
            }
        }
        // Now cache miss or new data arrives.
        // Put current data in table first.
        table[key] = data
        // Now perform full table scan in parallel.
        val result = ConcurrentHashMap<String, Int>()
        table.entries.parallelStream().forEach { (k, v) ->
            if (k != key) {
                val count = v.intersectionSize(data, wsize)
                if (count > 0) {
                    result[k] = count
                } else if (count == -1) {
                    logger.warn { "Unable to compute intersect size for <$data> and <$v>" }
                }
            }
        }
        // Write non-empty result to cache
        if (result.isNotEmpty()) {
            cache.put(dataBuf, result)
            // Update cache entries in parallel
            cache.asMap().entries.parallelStream()
                .forEach { (buf, res) ->
                    if (buf != dataBuf) {
                        // The map key of type `ByteBuf` must have a backing array (Caller should guarantee this), so we use
                        // ByteArray.intersectionSize(...) extension function instead of ByteBuf.intersectionSize(...)
                        // to avoid `ByteBuf` overhead.
                        val newSize = buf.array().intersectionSize(data, wsize)
                        val oldSize = res[key]
                        if (newSize > 0 && newSize != oldSize) {
                            res[key] = newSize
                        }
                    }
                }
        }
        return result
    }
}
