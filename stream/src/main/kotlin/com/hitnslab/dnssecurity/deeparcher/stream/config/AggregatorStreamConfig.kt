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
import org.apache.kafka.streams.kstream.Produced
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.xbill.DNS.*
import java.net.InetAddress
import java.time.Duration


@Configuration
@EnableConfigurationProperties(AggregatorProperties::class)
//@ConditionalOnBean(AggregatorProperties::class)
@ConditionalOnProperty(prefix = "app.aggregator", name = ["enabled"], havingValue = "true")
class AggregatorStreamConfig : AppStreamConfigurer() {

    @Autowired
    lateinit var properties: AggregatorProperties

    private val logger = KotlinLogging.logger {}

    private val backgroundScope = CoroutineScope(newSingleThreadContext("aggregator"))

    private var resolver: Resolver? = null

    @Bean
    fun aggregatorStream(builder: StreamsBuilder): KStream<*, *> {
        if (properties.lookupTimeout != 0L) {
            val resolv = ExtendedResolver()
            if (properties.lookupTimeout != -1L) {
                resolv.timeout = Duration.ofMillis(properties.lookupTimeout)
            }
            resolver = resolv
        }
        val src = builder.stream(
            properties.input.path,
            Consumed.with(
                Serdes.String(),
                GenericSerde(PDnsProtoSerializer::class, PDnsProtoDeserializer::class)
            )
        )
        val table = src
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
                    aggBuilder.build()
                },
                Materialized.with(
                    Serdes.String(),
                    GenericSerde(DomainIPAssocDetailProtoSerializer::class, DomainIPAssocDetailProtoDeserializer::class)
                )
            )
        val updates: KStream<String, DomainIPAssocDetailProto.DomainIPAssocDetail>
        val resolv = resolver
        if (resolv != null) {
            updates = table
                .mapValues { _, v ->
                    val agg = DomainIPAssocDetailProto.DomainIPAssocDetail
                        .newBuilder()
                        .mergeFrom(v)
                    postAggregationCheck(agg, resolv)
                }
                .mapValues { _, v ->
                    runBlocking {
                        v.await()
                        v.getCompleted().build()
                    }
                }
                .toStream()
        } else {
            updates = table.toStream()
        }

        properties.output.forEach {
            if (it.type == "topic") {
                updates.to(
                    it.path,
                    Produced.with(
                        Serdes.String(),
                        GenericSerde(
                            DomainIPAssocDetailProtoSerializer::class,
                            DomainIPAssocDetailProtoDeserializer::class
                        )
                    )
                )
            } else {
                logger.warn { "Unsupported sink type <${it.type}>" }
            }
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
    private fun postAggregationCheck(
        builder: DomainIPAssocDetailProto.DomainIPAssocDetail.Builder,
        resolver: Resolver
    ) =
        backgroundScope.async {
            val jobs = mutableListOf<Job>()
            var checkRest = false
            var found = false
            if (builder.ipv4Addrs.isEmpty) {
                logger.info { "Now performing DNS lookup for <${builder.fqdn}>" }
                checkRest = true
                jobs.add(
                    launch {
                        logger.debug { "After one aggregation step, <${builder.fqdn}> has no IPv4 address" }
                        val lookup = Lookup(builder.fqdn, Type.A)
                        lookup.setResolver(resolver)
                        withContext(Dispatchers.IO) { lookup.run() }?.let { records ->
                            val ips = mutableSetOf<InetAddress>()
                            records.forEach { ips.add((it as ARecord).address) }
                            if (ips.isNotEmpty()) {
                                val ipBytes = ByteBufAllocator.DEFAULT.heapBuffer(ips.size * 4)
                                ips.forEach { ipBytes.writeBytes(it.address) }
                                builder.ipv4Addrs = ByteString.copyFrom(ipBytes.nioBuffer())
                                ipBytes.release()
                                found = true
                            }
                        }
                    }
                )
            }
            if (checkRest && builder.ipv6Addrs.isEmpty) {
                jobs.add(
                    launch {
                        logger.debug { "After one aggregation step, <${builder.fqdn}> has no IPv6 address" }
                        val lookup = Lookup(builder.fqdn, Type.AAAA)
                        lookup.setResolver(resolver)
                        withContext(Dispatchers.IO) { lookup.run() }?.let { records ->
                            val ips = mutableSetOf<InetAddress>()
                            records.forEach { ips.add((it as AAAARecord).address) }
                            if (ips.isNotEmpty()) {
                                val ipBytes = ByteBufAllocator.DEFAULT.heapBuffer(ips.size * 16)
                                ips.forEach { ipBytes.writeBytes(it.address) }
                                builder.ipv6Addrs = ByteString.copyFrom(ipBytes.nioBuffer())
                                ipBytes.release()
                                found = true
                            }
                        }
                    }
                )
            } else {
                checkRest = false
            }
            if (checkRest && builder.cnamesCount == 0) {
                jobs.add(
                    launch {
                        logger.debug { "After one aggregation step, <${builder.fqdn}> has no CNAME" }
                        val cnames = mutableSetOf<String>()
                        val lookup = Lookup(builder.fqdn, Type.CNAME)
                        lookup.setResolver(resolver)
                        withContext(Dispatchers.IO) { lookup.run() }?.forEach {
                            cnames.add((it as CNAMERecord).target.toString(true))
                        }
                        builder.addAllCnames(cnames)
                        found = true
                    }
                )
            }
            jobs.forEach { job ->
                job.join()
                if (found) return@forEach
            }
            if (builder.ipv4Addrs.isEmpty && builder.ipv6Addrs.isEmpty && builder.cnamesCount == 0) {
                logger.warn { "After final checks of one aggregation step, <${builder.fqdn}> has no A, AAAA, CNAME value" }
            }
            builder
        }
}