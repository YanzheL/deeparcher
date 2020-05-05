package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.hitnslab.dnssecurity.deeparcher.serde.*
import com.hitnslab.dnssecurity.deeparcher.stream.processor.GraphEdgeGenerator
import com.hitnslab.dnssecurity.deeparcher.stream.property.GraphProperties
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Produced
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(GraphProperties::class)
@ConditionalOnProperty(prefix = "app.graph", name = ["enabled"], havingValue = "true")
class GraphStreamConfig : AppStreamConfigurer() {

    @Autowired
    lateinit var properties: GraphProperties

    private val logger = KotlinLogging.logger {}

    @Bean
    fun stream(builder: StreamsBuilder): KStream<*, *> {
        val src = builder.stream(
            properties.input.path,
            Consumed.with(
                Serdes.String(),
                GenericSerde(DomainAssocDetailProtoSerializer::class, DomainAssocDetailProtoDeserializer::class)
            )
        )
        src
            .flatMapValues(
                GraphEdgeGenerator()
            )
            .to(
                properties.output.path,
                Produced.with(
                    Serdes.String(),
                    GenericSerde(
                        GraphAssocEdgeUpdateProtoSerializer::class,
                        GraphAssocEdgeUpdateProtoDeserializer::class
                    )
                )
            )
        return src
    }
}