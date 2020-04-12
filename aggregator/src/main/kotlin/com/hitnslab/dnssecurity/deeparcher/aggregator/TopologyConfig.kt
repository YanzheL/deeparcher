package com.hitnslab.dnssecurity.deeparcher.aggregator

import com.github.benmanes.caffeine.cache.Caffeine
import com.hitnslab.dnssecurity.deeparcher.model.DomainIPAssocDetail
import com.hitnslab.dnssecurity.deeparcher.serde.DomainIPAssocDetailSerde
import com.hitnslab.dnssecurity.deeparcher.serde.PDnsSerde
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Materialized
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.net.Inet4Address
import java.net.Inet6Address

@Configuration
@EnableConfigurationProperties(AppProperties::class)
class TopologyConfig : DisposableBean {

    @Autowired
    lateinit var appProperties: AppProperties

    private var fileIO = false

    private val logger = KotlinLogging.logger {}

    val fileSinkWriters by lazy {
        Caffeine.newBuilder()
                .maximumSize(100)
                .removalListener<String, PrintWriter> { _, v, cause ->
                    v?.close()
                    logger.info { "Writer-Cache evicted <$v> for cause <$cause>" }
                }
                .build<String, PrintWriter> {
                    fileIO = true
                    PrintWriter(FileWriter(File(it), true), true)
                }
    }

    override fun destroy() {
        if (!fileIO) {
            return
        }
        logger.info { "Destroying fileSinkWriters..." }
        fileSinkWriters.invalidateAll()
        logger.info { "Destroyed fileSinkWriters" }
    }

    @Bean
    fun topology(): Topology {
        val builder = StreamsBuilder()
        val src = builder.stream(
                appProperties.input.path,
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
        appProperties.output.forEach {
            configSinks(updates, it.type, it.path, it.options)
        }
        return builder.build()
    }

    fun configSinks(src: KStream<String, *>, type: String, path: String, options: Map<String, String>) {
        val sinkSrc = src
        when (type) {
            "topic" -> sinkSrc.to(path)
            "file" -> sinkSrc.foreach { _, v ->
                fileSinkWriters.get(path)!!.println(v)
            }
            else -> throw Exception("Invalid sink type <$type>")
        }
    }
}