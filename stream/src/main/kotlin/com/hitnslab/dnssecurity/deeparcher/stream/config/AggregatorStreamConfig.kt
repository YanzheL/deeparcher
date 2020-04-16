package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.hitnslab.dnssecurity.deeparcher.model.DomainIPAssocDetail
import com.hitnslab.dnssecurity.deeparcher.serde.DomainIPAssocDetailSerde
import com.hitnslab.dnssecurity.deeparcher.serde.PDnsSerde
import com.hitnslab.dnssecurity.deeparcher.stream.property.AggregatorProperties
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
import java.net.Inet4Address
import java.net.Inet6Address


@Configuration
@EnableConfigurationProperties(AggregatorProperties::class)
//@ConditionalOnBean(AggregatorProperties::class)
@ConditionalOnProperty(prefix = "app.aggregator", name = ["enabled"], havingValue = "true")
class AggregatorStreamConfig : AppStreamConfigurer() {

    @Autowired
    lateinit var properties: AggregatorProperties

    @Bean
    fun aggregatorStream(builder: StreamsBuilder): KStream<*, *> {
        val src = builder.stream(
                properties.input.path,
                Consumed.with(Serdes.String(), PDnsSerde())
        )
        val updates = src
                .mapValues { _, v ->
                    val ret = DomainIPAssocDetail(v.domain, v.topPrivateDomain)
                    v.ips?.forEach {
                        when (it) {
                            is Inet4Address -> ret.ipv4Addresses.add(it)
                            is Inet6Address -> ret.ipv6Addresses.add(it)
                        }
                    }
                    ret
                }
                .groupByKey()
                .reduce(
                        { v1, v2 ->
                            v1.ipv4Addresses.addAll(v2.ipv4Addresses)
                            v1.ipv6Addresses.addAll(v2.ipv6Addresses)
                            v1
                        },
                        Materialized.with(
                                Serdes.String(),
                                DomainIPAssocDetailSerde()
                        )
                )
                .toStream()
        properties.output.forEach {
            configSinks(updates, it.type, it.path, it.options)
        }
        return src
    }

}