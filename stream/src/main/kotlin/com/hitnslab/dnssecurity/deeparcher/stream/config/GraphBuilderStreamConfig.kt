package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.google.common.hash.BloomFilter
import com.google.common.hash.Funnels
import com.google.common.net.InternetDomainName
import com.hitnslab.dnssecurity.deeparcher.serde.*
import com.hitnslab.dnssecurity.deeparcher.stream.processor.GraphEventGenerator
import com.hitnslab.dnssecurity.deeparcher.stream.property.GraphBuilderProperties
import com.hitnslab.dnssecurity.deeparcher.stream.service.MongoStringIdService
import com.mongodb.client.MongoClient
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Produced
import org.apache.kafka.streams.kstream.ValueTransformerSupplier
import org.bson.Document
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
@Configuration
@EnableConfigurationProperties(GraphBuilderProperties::class)
@ConditionalOnProperty(prefix = "app.graph-builder", name = ["enabled"], havingValue = "true")
class GraphBuilderStreamConfig : AppStreamConfigurer() {

    @Autowired
    lateinit var builderProperties: GraphBuilderProperties

    @Autowired
    lateinit var mongoClient: MongoClient

    private val logger = KotlinLogging.logger {}

    @Bean
    fun stream(builder: StreamsBuilder): KStream<*, *> {
        val src = builder.stream(
            builderProperties.input.path,
            Consumed.with(
                Serdes.String(),
                GenericSerde(DomainDnsDetailProtoSerializer::class, DomainDnsDetailProtoDeserializer::class)
            )
        )
        val nodeIdServiceProps = builderProperties.nodeIdService
        val nodeIdService = MongoStringIdService(
            nodeIdServiceProps.database,
            nodeIdServiceProps.collection,
            mongoClient,
            nodeIdServiceProps.keyField,
            nodeIdServiceProps.valueField
        ) { (k, _) ->
            listOf(
                Document("fqdn", k),
                Document("domain", InternetDomainName.from(k).topPrivateDomain().toString())
            )
        }
        var sinkSrc = src
            .flatTransformValues(ValueTransformerSupplier {
                GraphEventGenerator(
                    nodeIdService,
                    builderProperties.cacheLimit,
                    builderProperties.commitInterval
                )
            })
        val outputOpts = builderProperties.output.options
        val unique = outputOpts.getOrDefault("unique", "false").toBoolean()
        if (unique) {
            val filter = BloomFilter.create(
                Funnels.integerFunnel(),
                outputOpts.getOrDefault("expectedInsertions", "100000000").toLong(),
                outputOpts.getOrDefault("fpp", "0.01").toDouble()
            )
            sinkSrc = sinkSrc.filterNot { _, v ->
                val hashcode = v.hashCode()
                val seen = filter.mightContain(hashcode)
                filter.put(hashcode)
                return@filterNot seen
            }
        }
        sinkSrc
            .to(
                builderProperties.output.path,
                Produced.with(
                    Serdes.String(),
                    GenericSerde(
                        GraphEventProtoSerializer::class,
                        GraphEventProtoDeserializer::class
                    )
                )
            )
        return src
    }
}