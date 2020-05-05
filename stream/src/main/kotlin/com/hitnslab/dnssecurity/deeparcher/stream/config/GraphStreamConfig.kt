package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.hitnslab.dnssecurity.deeparcher.serde.DomainAssocDetailProtoDeserializer
import com.hitnslab.dnssecurity.deeparcher.serde.DomainAssocDetailProtoSerializer
import com.hitnslab.dnssecurity.deeparcher.serde.GenericSerde
import com.hitnslab.dnssecurity.deeparcher.serde.JsonSerde
import com.hitnslab.dnssecurity.deeparcher.stream.processor.GraphEdgeGenerator
import com.hitnslab.dnssecurity.deeparcher.stream.property.GraphProperties
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.ValueTransformerSupplier
import org.apache.kafka.streams.state.Stores
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
        builder.addStateStore(
            Stores.keyValueStoreBuilder(
                Stores.inMemoryKeyValueStore("graph-edge-generator.ipv4"),
                Serdes.String(),
                Serdes.ByteArray()
            )
        )
        builder.addStateStore(
            Stores.keyValueStoreBuilder(
                Stores.inMemoryKeyValueStore("graph-edge-generator.ipv6"),
                Serdes.String(),
                Serdes.ByteArray()
            )
        )
        builder.addStateStore(
            Stores.keyValueStoreBuilder(
                Stores.inMemoryKeyValueStore("graph-edge-generator.cname"),
                Serdes.String(),
                JsonSerde(Set::class)
            )
        )

        val src = builder.stream(
            properties.input.path,
            Consumed.with(
                Serdes.String(),
                GenericSerde(DomainAssocDetailProtoSerializer::class, DomainAssocDetailProtoDeserializer::class)
            )
        )
            .flatTransformValues(
                ValueTransformerSupplier { GraphEdgeGenerator() },
                "graph-edge-generator.ipv4",
                "graph-edge-generator.ipv6",
                "graph-edge-generator.cname"
            )
        src
            .to(properties.output.path)
        return src
    }
}