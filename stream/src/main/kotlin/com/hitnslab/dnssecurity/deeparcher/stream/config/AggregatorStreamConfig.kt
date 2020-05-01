package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.google.protobuf.ByteString
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainIPAssocDetailProto
import com.hitnslab.dnssecurity.deeparcher.serde.*
import com.hitnslab.dnssecurity.deeparcher.stream.property.AggregatorProperties
import com.hitnslab.dnssecurity.deeparcher.util.bytesSetUnion
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.Unpooled
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Materialized
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.xbill.DNS.*
import java.net.InetAddress


@Configuration
@EnableConfigurationProperties(AggregatorProperties::class)
//@ConditionalOnBean(AggregatorProperties::class)
@ConditionalOnProperty(prefix = "app.aggregator", name = ["enabled"], havingValue = "true")
class AggregatorStreamConfig : AppStreamConfigurer() {

    @Autowired
    lateinit var properties: AggregatorProperties

    private val logger = KotlinLogging.logger {}

    @Bean
    fun aggregatorStream(builder: StreamsBuilder): KStream<*, *> {
        val src = builder.stream(
            properties.input.path,
            Consumed.with(
                Serdes.String(),
                GenericSerde(PDnsProtoSerializer::class, PDnsProtoDeserializer::class)
            )
        )
        val updates = src
            .groupByKey()
            .aggregate(
                { DomainIPAssocDetailProto.DomainIPAssocDetail.getDefaultInstance() },
                { k, v, agg ->
                    val aggBuilder = DomainIPAssocDetailProto.DomainIPAssocDetail
                        .newBuilder()
                        .mergeFrom(agg)
                    var firstSeen = false
                    if (aggBuilder.fqdn.isEmpty()) {
                        firstSeen = true
                        aggBuilder.fqdn = k
                    }
                    if (aggBuilder.domain.isEmpty()) {
                        aggBuilder.domain = v.domain
                    }
                    // New CNAMEs for current step
                    val cnames by lazy { mutableSetOf<String>() }

                    if (firstSeen) {
                        // Perform DNS lookup if aggregation value is in initial state.
                        // This tries to ensure that every aggregation value has current IP
                        val recordsA = Lookup(v.fqdn, Type.A).run()
                        val recordsAAAA = Lookup(v.fqdn, Type.AAAA).run()
                        val recordsCNAME = Lookup(v.fqdn, Type.CNAME).run()
                        recordsA?.let { records ->
                            val ips = mutableSetOf<InetAddress>()
                            records.forEach { ips.add((it as ARecord).address) }
                            if (ips.isNotEmpty()) {
                                val ipBytes = ByteBufAllocator.DEFAULT.heapBuffer(ips.size * 4)
                                ips.forEach { ipBytes.writeBytes(it.address) }
                                aggBuilder.ipv4Addrs = ByteString.copyFrom(ipBytes.nioBuffer())
                                ipBytes.release()
                            }
                        }
                        recordsAAAA?.let { records ->
                            val ips = mutableSetOf<InetAddress>()
                            records.forEach { ips.add((it as AAAARecord).address) }
                            if (ips.isNotEmpty()) {
                                val ipBytes = ByteBufAllocator.DEFAULT.heapBuffer(ips.size * 16)
                                ips.forEach { ipBytes.writeBytes(it.address) }
                                aggBuilder.ipv6Addrs = ByteString.copyFrom(ipBytes.nioBuffer())
                                ipBytes.release()
                            }
                        }
                        recordsCNAME?.forEach { cnames.add((it as CNAMERecord).target.toString(true)) }
                    }
                    if (!v.rIpv4Addrs.isEmpty) {
                        if (aggBuilder.ipv4Addrs.isEmpty) {
                            aggBuilder.ipv4Addrs = v.rIpv4Addrs
                        } else {
                            bytesSetUnion(
                                Unpooled.wrappedBuffer(aggBuilder.ipv4Addrs.asReadOnlyByteBuffer()),
                                Unpooled.wrappedBuffer(v.rIpv4Addrs.asReadOnlyByteBuffer()),
                                4
                            )?.let {
                                aggBuilder.ipv4Addrs = ByteString.copyFrom(it.nioBuffer())
                                it.release()
                            }
                        }
                    }
                    if (!v.rIpv6Addrs.isEmpty) {
                        if (aggBuilder.ipv6Addrs.isEmpty) {
                            aggBuilder.ipv6Addrs = v.rIpv6Addrs
                        } else {
                            bytesSetUnion(
                                Unpooled.wrappedBuffer(aggBuilder.ipv6Addrs.asReadOnlyByteBuffer()),
                                Unpooled.wrappedBuffer(v.rIpv6Addrs.asReadOnlyByteBuffer()),
                                16
                            )?.let {
                                aggBuilder.ipv4Addrs = ByteString.copyFrom(it.nioBuffer())
                                it.release()
                            }
                        }
                    }
                    if (v.rCnamesCount != 0) {
                        cnames.addAll(v.rCnamesList)
                        if (aggBuilder.cnamesCount == 0) {
                            aggBuilder.addAllCnames(cnames)
                        } else {
                            val aggCNames = aggBuilder.cnamesList.toMutableSet()
                            if (aggCNames.addAll(cnames)) {
                                aggBuilder.clearCnames()
                                aggBuilder.addAllCnames(aggCNames)
                            }
                        }
                    }
                    when {
                        aggBuilder.ipv4Addrs.isEmpty -> logger.info { "After one aggregation step, <${v.fqdn}> has no IPv4 address" }
                        aggBuilder.ipv6Addrs.isEmpty -> logger.info { "After one aggregation step, <${v.fqdn}> has no IPv6 address" }
                        aggBuilder.cnamesCount == 0 -> logger.info { "After one aggregation step, <${v.fqdn}> has no CNAME" }
                    }
                    aggBuilder.build()
                },
                Materialized.with(
                    Serdes.String(),
                    GenericSerde(DomainIPAssocDetailProtoSerializer::class, DomainIPAssocDetailProtoDeserializer::class)
                )
            )
            .toStream()
        properties.output.forEach {
            configSinks(updates, it.type, it.path, it.options)
        }
        return src
    }

}