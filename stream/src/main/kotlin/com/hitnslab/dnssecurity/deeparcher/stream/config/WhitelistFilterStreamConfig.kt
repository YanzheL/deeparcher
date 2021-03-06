package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.PDnsDataProto
import com.hitnslab.dnssecurity.deeparcher.serde.GenericSerde
import com.hitnslab.dnssecurity.deeparcher.serde.PDnsProtoDeserializer
import com.hitnslab.dnssecurity.deeparcher.serde.PDnsProtoSerializer
import com.hitnslab.dnssecurity.deeparcher.stream.KStreamValuePredicateAdapter
import com.hitnslab.dnssecurity.deeparcher.stream.property.WhitelistFilterProperties
import com.hitnslab.dnssecurity.deeparcher.util.DomainWhitelistPredicate
import com.hitnslab.dnssecurity.deeparcher.util.ProtobufMessagePredicate
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.apache.kafka.streams.kstream.Predicate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.UrlResource

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
@Configuration
@EnableConfigurationProperties(WhitelistFilterProperties::class)
@ConditionalOnProperty(prefix = "app.whitelist-filter", name = ["enabled"], havingValue = "true")
class WhitelistFilterStreamConfig : AppStreamConfigurer() {

    @Autowired
    lateinit var properties: WhitelistFilterProperties


    @Bean
    fun whitelistFilterStream(builder: StreamsBuilder): KStream<*, *> {
        val prefilters = mutableListOf<Predicate<String, PDnsDataProto.PDnsData>>()
        properties.prefilters?.forEach {
            prefilters.add(
                KStreamValuePredicateAdapter(
                    ProtobufMessagePredicate(
                        it.field,
                        Regex(it.pattern),
                        it.allow
                    )
                )
            )
        }
        val src = builder.stream(
            properties.input.path,
            Consumed.with(
                Serdes.String(),
                GenericSerde(PDnsProtoSerializer::class, PDnsProtoDeserializer::class)
            )
        )
        var srcPrefiltered = src
        prefilters.forEach {
            srcPrefiltered = src.filter(it)
        }
        val whitelistPredicate = DomainWhitelistPredicate()
        properties.whitelists.forEach {
            when (it.type) {
                "file" -> whitelistPredicate.fromResource(UrlResource(it.path))
                "regex" -> whitelistPredicate.fromRegex(it.path)
                else -> throw Exception("Invalid whitelist source type <${it.type}>")
            }
        }
        val matchRecords = srcPrefiltered
            .filter { _, v -> whitelistPredicate.test(v.domain) }
        val missRecords = srcPrefiltered
            .filterNot { _, v -> whitelistPredicate.test(v.domain) }
        properties.output.match.forEach {
            configSinks(matchRecords, it.type, it.path, it.options)
        }
        properties.output.miss.forEach {
            configSinks(missRecords, it.type, it.path, it.options)
        }
        return src
    }
}