package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.hitnslab.dnssecurity.deeparcher.serde.PDnsSerde
import com.hitnslab.dnssecurity.deeparcher.stream.PDnsPrefilter
import com.hitnslab.dnssecurity.deeparcher.stream.WhitelistPredicate
import com.hitnslab.dnssecurity.deeparcher.stream.property.WhitelistFilterProperties
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(WhitelistFilterProperties::class)
@ConditionalOnProperty(prefix = "app.whitelist-filter", name = ["enabled"], havingValue = "true")
class WhitelistFilterStreamConfig : AppStreamConfigurer() {

    @Autowired
    lateinit var properties: WhitelistFilterProperties


    @Bean
    fun whitelistFilterStream(builder: StreamsBuilder): KStream<*, *> {
        val prefilters = mutableListOf<PDnsPrefilter>()
        properties.prefilters?.forEach {
            prefilters.add(PDnsPrefilter(it.field, it.pattern, it.allow))
        }
        val src = builder.stream(
                properties.input.path,
                Consumed.with(Serdes.String(), PDnsSerde())
        )
        var srcPrefiltered = src
        prefilters.forEach {
            srcPrefiltered = src.filter(it)
        }
        val whitelistPredicate = WhitelistPredicate()
        properties.whitelists.forEach {
            when (it.type) {
                "file" -> whitelistPredicate.fromFile(it.path)
                "regex" -> whitelistPredicate.fromRegex(it.path)
                else -> throw Exception("Invalid whitelist source type <${it.type}>")
            }
        }
        val matchRecords = srcPrefiltered
                .filter { _, v -> whitelistPredicate.test(v.topPrivateDomain) }
        val missRecords = srcPrefiltered
                .filterNot { _, v -> whitelistPredicate.test(v.topPrivateDomain) }
        properties.output.match.forEach {
            configSinks(matchRecords, it.type, it.path, it.options)
        }
        properties.output.miss.forEach {
            configSinks(missRecords, it.type, it.path, it.options)
        }
        return src
    }
}