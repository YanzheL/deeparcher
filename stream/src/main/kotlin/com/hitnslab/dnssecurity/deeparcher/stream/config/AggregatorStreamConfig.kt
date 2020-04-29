package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.google.protobuf.ByteString
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainIPAssocDetailProto
import com.hitnslab.dnssecurity.deeparcher.serde.*
import com.hitnslab.dnssecurity.deeparcher.stream.property.AggregatorProperties
import com.hitnslab.dnssecurity.deeparcher.util.ByteBufSet
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
                            val bufSet = ByteBufSet(aggBuilder.ipv4Addrs.asReadOnlyByteBuffer())
                            bufSet.addAll(Unpooled.wrappedBuffer(v.rIpv4Addrs.asReadOnlyByteBuffer()), 4)
                            if (bufSet.dirty) {
                                aggBuilder.ipv4Addrs = ByteString.copyFrom(bufSet.buffer.nioBuffer())
                            }
                            bufSet.release()
                        }
                    }
                    if (!v.rIpv6Addrs.isEmpty) {
                        if (aggBuilder.ipv6Addrs.isEmpty) {
                            aggBuilder.ipv6Addrs = v.rIpv6Addrs
                        } else {
                            val bufSet = ByteBufSet(aggBuilder.ipv6Addrs.asReadOnlyByteBuffer())
                            bufSet.addAll(Unpooled.wrappedBuffer(v.rIpv6Addrs.asReadOnlyByteBuffer()), 16)
                            if (bufSet.dirty) {
                                aggBuilder.ipv6Addrs = ByteString.copyFrom(bufSet.buffer.nioBuffer())
                            }
                            bufSet.release()
                        }
                    }
                    if (v.rCnamesCount != 0) {
                        if (aggBuilder.cnamesCount == 0) {
                            aggBuilder.addAllCnames(v.rCnamesList)
                        } else {
                            val cnameSet = aggBuilder.cnamesList.toMutableSet()
                            if (cnameSet.addAll(v.rCnamesList)) {
                                aggBuilder.clearCnames()
                                aggBuilder.addAllCnames(cnameSet)
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
            .toStream()
        properties.output.forEach {
            configSinks(updates, it.type, it.path, it.options)
        }
        return src
    }

}