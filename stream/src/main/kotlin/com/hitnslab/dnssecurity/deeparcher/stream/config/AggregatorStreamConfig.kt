package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.google.protobuf.ByteString
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainIPAssocDetailProto
import com.hitnslab.dnssecurity.deeparcher.serde.*
import com.hitnslab.dnssecurity.deeparcher.stream.property.AggregatorProperties
import com.hitnslab.dnssecurity.deeparcher.util.bytesSetUnion
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.Unpooled
import kotlinx.coroutines.*
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
                    if (aggBuilder.fqdn.isEmpty()) {
                        aggBuilder.fqdn = k
                    }
                    if (aggBuilder.domain.isEmpty()) {
                        aggBuilder.domain = v.domain
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
                        if (aggBuilder.cnamesCount == 0) {
                            aggBuilder.addAllCnames(v.rCnamesList)
                        } else {
                            val aggCNames = aggBuilder.cnamesList.toMutableSet()
                            if (aggCNames.addAll(v.rCnamesList)) {
                                aggBuilder.clearCnames()
                                aggBuilder.addAllCnames(aggCNames)
                            }
                        }
                    }
                    postAggregationCheck(aggBuilder)
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

    /**
     * Perform DNS lookup if aggregation of some DNS related field is empty
     * This tries to ensure that every aggregation field is not empty
     * If high priority field is not empty, then skip checking rest fields, because DNS lookup takes too much time.
     *
     * @param builder: Current aggregation result
     */
    private fun postAggregationCheck(builder: DomainIPAssocDetailProto.DomainIPAssocDetail.Builder) = runBlocking {
        val jobs = mutableListOf<Job>()
        var checkRest = false
        var allEmpty = true
        if (builder.ipv4Addrs.isEmpty) {
            checkRest = true
            jobs.add(
                launch {
                    logger.info { "After one aggregation step, <${builder.fqdn}> has no IPv4 address, now performing DNS lookup" }
                    withContext(Dispatchers.IO) { Lookup(builder.fqdn, Type.A).run() }?.let { records ->
                        val ips = mutableSetOf<InetAddress>()
                        records.forEach { ips.add((it as ARecord).address) }
                        if (ips.isNotEmpty()) {
                            val ipBytes = ByteBufAllocator.DEFAULT.heapBuffer(ips.size * 4)
                            ips.forEach { ipBytes.writeBytes(it.address) }
                            builder.ipv4Addrs = ByteString.copyFrom(ipBytes.nioBuffer())
                            ipBytes.release()
                            checkRest = false
                            allEmpty = false
                        }
                    }
                }
            )
        } else {
            allEmpty = false
        }
        if (builder.ipv6Addrs.isEmpty) {
            jobs.add(
                launch {
                    logger.info { "After one aggregation step, <${builder.fqdn}> has no IPv6 address" }
                    withContext(Dispatchers.IO) { Lookup(builder.fqdn, Type.AAAA).run() }?.let { records ->
                        val ips = mutableSetOf<InetAddress>()
                        records.forEach { ips.add((it as AAAARecord).address) }
                        if (ips.isNotEmpty()) {
                            val ipBytes = ByteBufAllocator.DEFAULT.heapBuffer(ips.size * 16)
                            ips.forEach { ipBytes.writeBytes(it.address) }
                            builder.ipv6Addrs = ByteString.copyFrom(ipBytes.nioBuffer())
                            ipBytes.release()
                            checkRest = false
                            allEmpty = false
                        }
                    }
                }
            )
        } else {
            allEmpty = false
        }
        if (builder.cnamesCount == 0) {
            jobs.add(
                launch {
                    logger.info { "After one aggregation step, <${builder.fqdn}> has no CNAME" }
                    val cnames = mutableSetOf<String>()
                    withContext(Dispatchers.IO) { Lookup(builder.fqdn, Type.CNAME).run() }?.forEach {
                        cnames.add((it as CNAMERecord).target.toString(true))
                        allEmpty = false
                    }
                    builder.addAllCnames(cnames)
                }
            )
        } else {
            allEmpty = false
        }
        jobs.forEach { job ->
            job.join()
            if (!checkRest) return@forEach
        }
        if (allEmpty) {
            logger.warn { "After final checks of one aggregation step, <${builder.fqdn}> has no A, AAAA, CNAME value" }
        }
    }

}