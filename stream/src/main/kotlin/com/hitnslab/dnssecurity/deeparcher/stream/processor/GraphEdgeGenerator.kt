package com.hitnslab.dnssecurity.deeparcher.stream.processor

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainAssocDetailProto.DomainAssocDetail
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate
import com.hitnslab.dnssecurity.deeparcher.util.ByteBufSet
import io.netty.buffer.Unpooled
import kotlinx.coroutines.*
import mu.KotlinLogging
import org.apache.kafka.streams.kstream.ValueTransformer
import org.apache.kafka.streams.processor.ProcessorContext
import org.apache.kafka.streams.state.KeyValueStore
import java.util.concurrent.ConcurrentHashMap

class GraphEdgeGenerator : ValueTransformer<DomainAssocDetail, Iterable<GraphAssocEdgeUpdate>> {

    lateinit var context: ProcessorContext

    lateinit var ipv4: KeyValueStore<String, ByteArray>

    lateinit var ipv6: KeyValueStore<String, ByteArray>

    lateinit var cnames: KeyValueStore<String, Set<String>>

    val scope = CoroutineScope(Dispatchers.Default)

    private val logger = KotlinLogging.logger {}

    override fun init(ctx: ProcessorContext) {
        context = ctx
        ipv4 = ctx.getStateStore("graph-edge-generator.ipv4") as KeyValueStore<String, ByteArray>
        ipv6 = ctx.getStateStore("graph-edge-generator.ipv6") as KeyValueStore<String, ByteArray>
        cnames = ctx.getStateStore("graph-edge-generator.cname") as KeyValueStore<String, Set<String>>
    }

    override fun transform(value: DomainAssocDetail): Iterable<GraphAssocEdgeUpdate>? {
        val result = ConcurrentHashMap<String, Int>()
        val jobs = listOf(
            scope.launch { computeIPIntersect(value.fqdn, value.ipv4Addrs.toByteArray(), 4, ipv4, result) },
            scope.launch { computeIPIntersect(value.fqdn, value.ipv6Addrs.toByteArray(), 16, ipv6, result) },
            scope.launch { computeSetIntersect(value.fqdn, value.cnamesList.toSet(), cnames, result) }
        )
        runBlocking { jobs.forEach { job -> job.join() } }
        return generate(value.fqdn, result)
    }

    private fun generate(fqdn: String, result: Map<String, Int>): Iterable<GraphAssocEdgeUpdate>? {
        if (result.isEmpty()) {
            return null
        }
        return result.entries.map {
            GraphAssocEdgeUpdate
                .newBuilder()
                .setFqdn1(fqdn)
                .setFqdn2(it.key)
                .setNSharedFields(it.value)
                .build()
        }
    }

    private fun computeSetIntersect(
        fqdn: String,
        data: Set<String>,
        store: KeyValueStore<String, Set<String>>,
        result: MutableMap<String, Int>
    ) {
        store.put(fqdn, data)
        store.all().forEach { entry ->
            if (entry.key != fqdn) {
                val count = entry.value.intersect(data).size
                if (count > 0) {
                    result.merge(entry.key, count) { v1, v2 -> v1 + v2 }
                }
            }
        }
    }

    private fun computeIPIntersect(
        fqdn: String,
        ips: ByteArray,
        width: Int,
        store: KeyValueStore<String, ByteArray>,
        result: MutableMap<String, Int>
    ) {
        store.put(fqdn, ips)
        store.all().forEach { entry ->
            if (entry.key != fqdn) {
                val count = ByteBufSet.intersectionSize(
                    Unpooled.wrappedBuffer(ips),
                    Unpooled.wrappedBuffer(entry.value),
                    width
                )
                if (count > 0) {
                    result.merge(entry.key, count) { v1, v2 -> v1 + v2 }
                } else if (count == -1) {
                    logger.warn { "Unable to compute intersect size for <$ips> and <${entry.value}>" }
                }
            }
        }
    }

    override fun close() {
        scope.cancel()
    }
}