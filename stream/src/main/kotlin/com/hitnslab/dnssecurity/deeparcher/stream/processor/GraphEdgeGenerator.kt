package com.hitnslab.dnssecurity.deeparcher.stream.processor

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainAssocDetailProto.DomainAssocDetail
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate
import com.hitnslab.dnssecurity.deeparcher.util.ByteBufSet
import com.hitnslab.dnssecurity.deeparcher.util.intersectionSize
import io.netty.buffer.Unpooled
import mu.KotlinLogging
import org.apache.kafka.streams.kstream.ValueMapper

class GraphEdgeGenerator : ValueMapper<DomainAssocDetail, Iterable<GraphAssocEdgeUpdate>> {

    val ipv4Table = mutableMapOf<String, ByteArray>()

    val ipv6Table = mutableMapOf<String, ByteArray>()

    val cnamesTable = mutableMapOf<String, Collection<String>>()

    private val logger = KotlinLogging.logger {}

    override fun apply(value: DomainAssocDetail): Iterable<GraphAssocEdgeUpdate> {
        val result = mutableMapOf<String, Int>()
        computeIPIntersect(value.fqdn, value.ipv4Addrs.toByteArray(), 4, ipv4Table, result)
        computeIPIntersect(value.fqdn, value.ipv6Addrs.toByteArray(), 16, ipv6Table, result)
        computeSetIntersect(value.fqdn, value.cnamesList, cnamesTable, result)
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
        data: Collection<String>,
        store: MutableMap<String, Collection<String>>,
        result: MutableMap<String, Int>
    ) {
        if (data.isEmpty()) {
            return
        }
        store[fqdn] = data
        store.entries.parallelStream().forEach { entry ->
            if (entry.key != fqdn) {
                val count = entry.value.intersectionSize(data)
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
        if (ips.isEmpty()) {
            return
        }
        store[fqdn] = ips
        store.entries.parallelStream().forEach { entry ->
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
