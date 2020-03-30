package com.hitnslab.dnssecurity.deeparcher.whitelistfilter

import mu.KotlinLogging
import org.apache.kafka.streams.KafkaStreams
import org.apache.kafka.streams.Topology
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.config.KafkaStreamsConfiguration
import org.springframework.stereotype.Component

@EnableKafkaStreams
@Component
class CustomCommandLineRunner : CommandLineRunner, DisposableBean {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var kafkaStreamsConfiguration: KafkaStreamsConfiguration

    lateinit var stream: KafkaStreams

    @Autowired
    lateinit var topology: Topology

    override fun run(vararg args: String?) {
        stream = KafkaStreams(
                topology,
                kafkaStreamsConfiguration.asProperties()
        )
        logger.info { topology.describe().toString() }
        stream.cleanUp()
        stream.start()
    }

    override fun destroy() {
        logger.info { "Destroying CustomCommandLineRunner..." }
        stream.close()
        logger.info { "Destroyed CustomCommandLineRunner" }
    }
}