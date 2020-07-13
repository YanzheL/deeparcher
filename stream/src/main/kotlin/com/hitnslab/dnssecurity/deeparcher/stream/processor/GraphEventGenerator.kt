package com.hitnslab.dnssecurity.deeparcher.stream.processor

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.google.protobuf.Any
import com.google.protobuf.Int32Value
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.DomainDnsDetailProto.DomainDnsDetail
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.GraphEventProto.GraphEvent
import com.hitnslab.dnssecurity.deeparcher.stream.service.ObjectIdService
import com.hitnslab.dnssecurity.deeparcher.util.diffSize
import com.hitnslab.dnssecurity.deeparcher.util.intersectionSize
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import mu.KotlinLogging
import org.apache.kafka.streams.kstream.ValueTransformer
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.processor.PunctuationType
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * Generate weighed graph edges based on aggregated [DomainDnsDetail] inputs.
 *
 * Inputs are regarded as [KTable][org.apache.kafka.streams.kstream.KTable] changelog, and in-memory tables will be re-constructed based on that changelog.
 * For each input, it outputs all graph edge events with type [GraphEvent].
 * The edge weight represents the intersected items of IPv4/IPv6/CNAME sets between current input and a related row in table (zero-weighted edges are omitted).
 * Tables are scanned in parallel, and results are cached internally.
 * These in-memory tables will not persist between restarts, which means this is a non-persistent stateful operation.
 *
 * This is a thread-unsafe singleton class, because the order of [DomainDnsDetail] inputs should be maintained.
 *
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class GraphEventGenerator(
    val nodeIdService: ObjectIdService<String>,
    val cacheLimit: Long = 1000000L,
    val commitInterval: Long = 10
) :
    ValueTransformer<DomainDnsDetail, Iterable<GraphEvent>> {

    private val logger = KotlinLogging.logger {}

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

    override fun init(context: ProcessorContext) {
        context.schedule(Duration.ofSeconds(commitInterval), PunctuationType.WALL_CLOCK_TIME) {
            nodeIdService.commit()
        }
    }

    override fun transform(value: DomainDnsDetail): Iterable<GraphEvent> {
        val result = mutableMapOf<String, Int>()
        selectBytesIntersectedKeys(value.fqdn, value.ipv4Addrs.toByteArray(), 4, ipv4Table, ipv4Cache)
            .forEach { (k, v) -> result.merge(k, v) { v1, v2 -> v1 + v2 } }
        selectBytesIntersectedKeys(value.fqdn, value.ipv6Addrs.toByteArray(), 16, ipv6Table, ipv6Cache)
            .forEach { (k, v) -> result.merge(k, v) { v1, v2 -> v1 + v2 } }
        selectStringsIntersectedKeys(value.fqdn, value.cnamesList, cnameTable)
            .forEach { (k, v) -> result.merge(k, v) { v1, v2 -> v1 + v2 } }
        return result.map { (k, v) ->
            val node1 = nodeIdService.getOrCreateId(value.fqdn).toInt()
            val node2 = nodeIdService.getOrCreateId(k).toInt()
            val (v1, v2) = if (node1 < node2) node1.to(node2) else node2.to(node1)
            val attribute = Int32Value.newBuilder().setValue(v).build()
            GraphEvent
                .newBuilder()
                .setNode1(v1)
                .setNode2(v2)
                .putAttributes("weight", Any.pack(attribute))
                .build()
        }
    }

    override fun close() {
        nodeIdService.close()
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
}
