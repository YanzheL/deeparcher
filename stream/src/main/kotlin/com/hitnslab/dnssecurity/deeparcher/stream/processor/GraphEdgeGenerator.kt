package com.hitnslab.dnssecurity.deeparcher.stream.processor

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainAssocDetailProto.DomainAssocDetail
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate
import com.hitnslab.dnssecurity.deeparcher.util.ByteBufSet
import io.netty.buffer.Unpooled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.apache.kafka.streams.kstream.ValueMapper
import java.util.concurrent.ConcurrentHashMap

class GraphEdgeGenerator : ValueMapper<DomainAssocDetail, Iterable<GraphAssocEdgeUpdate>> {

    val ipv4 = mutableMapOf<String, ByteArray>()

    val ipv6 = mutableMapOf<String, ByteArray>()

    val cnames = mutableMapOf<String, Set<String>>()

    val scope = CoroutineScope(Dispatchers.Default)

    private val logger = KotlinLogging.logger {}

    override fun apply(value: DomainAssocDetail): Iterable<GraphAssocEdgeUpdate> {
        val result = ConcurrentHashMap<String, Int>()
        val jobs = listOf(
            scope.launch { computeIPIntersect(value.fqdn, value.ipv4Addrs.toByteArray(), 4, ipv4, result) },
            scope.launch { computeIPIntersect(value.fqdn, value.ipv6Addrs.toByteArray(), 16, ipv6, result) },
            scope.launch { computeSetIntersect(value.fqdn, value.cnamesList.toSet(), cnames, result) }
        )
        runBlocking { jobs.forEach { job -> job.join() } }
        return result.entries.map {
            GraphAssocEdgeUpdate
                .newBuilder()
                .setFqdn1(value.fqdn)
                .setFqdn2(it.key)
                .setNSharedFields(it.value)
                .build()
        }
    }

    private fun computeSetIntersect(
        fqdn: String,
        data: Set<String>,
        store: MutableMap<String, Set<String>>,
        result: MutableMap<String, Int>
    ) {
        store[fqdn] = data
        store.entries.forEach { entry ->
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
        store: MutableMap<String, ByteArray>,
        result: MutableMap<String, Int>
    ) {
        store[fqdn] = ips
        store.entries.forEach { entry ->
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
}