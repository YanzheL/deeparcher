package com.hitnslab.dnssecurity.deeparcher.stream.processor

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.DomainDnsDetailProto.DomainDnsDetail
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate
import com.hitnslab.dnssecurity.deeparcher.util.diffSize
import com.hitnslab.dnssecurity.deeparcher.util.intersectionSize
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import mu.KotlinLogging
import org.apache.kafka.streams.kstream.ValueMapper
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Generate weighed graph edges based on aggregated [DomainDnsDetail] inputs.
 *
 * Inputs are regarded as [KTable][org.apache.kafka.streams.kstream.KTable] changelog, and in-memory tables will be re-constructed based on that changelog.
 * For each input, it outputs all edges with type [GraphAssocEdgeUpdate].
 * The edge weight represents the intersected items of IPv4/IPv6/CNAME sets between current input and a related row in table (zero-weighted edges are omitted).
 * Tables are scanned in parallel, and results are cached internally.
 * These in-memory tables will not persist between restarts, which means this is a non-persistent stateful operation.
 *
 * This is a thread-unsafe singleton class, because the order of [DomainDnsDetail] inputs should be maintained.
 *
 * Call [GraphEdgeGenerator.getInstance] static method to get a singleton instance.
 *
 * TODO: Implement [ValueTransformer][org.apache.kafka.streams.kstream.ValueTransformer] interface instead of [ValueMapper]
 *
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class GraphEdgeGenerator private constructor() : ValueMapper<DomainDnsDetail, Iterable<GraphAssocEdgeUpdate>> {

    private val cacheLimit: Long = 1000000L

    private val ipv4Table = mutableMapOf<String, ByteArray>()

    private val ipv4Cache = Caffeine.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .maximumSize(cacheLimit)
        .build<ByteBuf, MutableMap<String, Int>>()

    private val ipv6Table = mutableMapOf<String, ByteArray>()

    private val ipv6Cache = Caffeine.newBuilder()
        .expireAfterAccess(10, TimeUnit.MINUTES)
        .maximumSize(cacheLimit)
        .build<ByteBuf, MutableMap<String, Int>>()

    private val cnameTable = mutableMapOf<String, Collection<String>>()

//    private val cnameCache = Caffeine.newBuilder()
//        .expireAfterAccess(10, TimeUnit.MINUTES)
//        .maximumSize(cacheSize)
//        .build<String, Collection<String>>()

    override fun apply(value: DomainDnsDetail): Iterable<GraphAssocEdgeUpdate> {
        val result = mutableMapOf<String, Int>()
        selectBytesIntersectedKeys(value.fqdn, value.ipv4Addrs.toByteArray(), 4, ipv4Table, ipv4Cache)
            .forEach { (k, v) -> result.merge(k, v) { v1, v2 -> v1 + v2 } }
        selectBytesIntersectedKeys(value.fqdn, value.ipv6Addrs.toByteArray(), 16, ipv6Table, ipv6Cache)
            .forEach { (k, v) -> result.merge(k, v) { v1, v2 -> v1 + v2 } }
        selectStringsIntersectedKeys(value.fqdn, value.cnamesList, cnameTable)
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
     * TODO: Support cached results.
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
    private fun selectStringsIntersectedKeys(
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
    private fun selectBytesIntersectedKeys(
        key: String,
        data: ByteArray,
        wsize: Int,
        table: MutableMap<String, ByteArray>,
        cache: Cache<ByteBuf, MutableMap<String, Int>>
    ): Map<String, Int> {
        if (data.isEmpty()) {
            return emptyMap()
        }
        val dataBuf = Unpooled.wrappedBuffer(data)
        // Check old data in table.
        val old = table[key]
        // Use cache only if no old data in table or old data is same as current data.
        if (old == null || data.diffSize(old, wsize) == 0) {
            val cached = cache.getIfPresent(dataBuf)
            if (cached != null) {
                if (old == null) {
                    // Put current data in table, otherwise the data will be lost.
                    table[key] = data
                }
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

    companion object {

        private lateinit var INSTANCE: GraphEdgeGenerator

        private val logger = KotlinLogging.logger {}

        @Synchronized
        fun getInstance(): GraphEdgeGenerator {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = GraphEdgeGenerator()
            }
            return INSTANCE
        }
    }
}
