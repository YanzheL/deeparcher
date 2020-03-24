package com.hitnslab.dnssecurity.deeparcher.whitelistfilter

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.common.hash.Funnels
import com.hitnslab.dnssecurity.deeparcher.serde.PDnsSerde
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.charset.Charset
import javax.annotation.PreDestroy

@EnableKafkaStreams
@Component
class CustomCommandLineRunner : CommandLineRunner {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var appProperties: AppProperties

    @Autowired
    lateinit var kafkaStreamsConfiguration: KafkaStreamsConfiguration

    lateinit var stream: KafkaStreams

    lateinit var topology: Topology

    val writers = Caffeine.newBuilder()
            .maximumSize(100)
            .removalListener<String, PrintWriter> { k, v, cause ->
                v?.close()
                logger.info { "Writer-Cache evicted <$v> for cause <$cause>" }
            }
            .build<String, PrintWriter> {
                PrintWriter(FileWriter(File(it), true), true)
            }

    override fun run(vararg args: String?) {
        topology = buildTopology()
        stream = KafkaStreams(
                topology,
                kafkaStreamsConfiguration.asProperties()
        )
        stream.cleanUp()
        stream.start()
    }

    @PreDestroy
    fun onDestroy() {
        logger.info { "Running On-Destroy hook..." }
        stream.close()
        writers.invalidateAll()
        logger.info { "Finished On-Destroy hook" }
    }

    private fun buildTopology(): Topology {
        val builder = StreamsBuilder()
        val validQTypes = listOf("A", "AAAA")
        val src = builder.stream(
                        appProperties.input.topic,
                        Consumed.with(Serdes.String(), PDnsSerde())
                )
                .filter { k, v -> v.queryType in validQTypes }
                .filter { k, v -> v.topPrivateDomain != "hitwh.edu.cn" }
        val whitelistPredicate = WhitelistPredicate(appProperties.whitelist.file)
        val inWhitelistUniq = src
                .filter { k, v -> whitelistPredicate.test(v.topPrivateDomain!!) }
                .filterNot(BloomFilterKStreamPredicate(
                        Funnels.stringFunnel(Charset.defaultCharset()),
                        5000000000,
                        0.01
                ))
        inWhitelistUniq.foreach { k, v ->
            writers.get("in-whitelist-uniq.txt")!!.println(k)
        }
        val notInWhitelistUniq = src
                .filterNot { k, v -> whitelistPredicate.test(v.topPrivateDomain!!) }
                .filterNot(BloomFilterKStreamPredicate(
                        Funnels.stringFunnel(Charset.defaultCharset()),
                        10000000,
                        0.0001
                ))
        notInWhitelistUniq.foreach { k, v -> writers.get("not-in-whitelist-uniq.txt")!!.println(k) }
        return builder.build()
    }
}