package com.hitnslab.dnssecurity.deeparcher.whitelistfilter

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.common.hash.Funnels
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import com.hitnslab.dnssecurity.deeparcher.serde.PDnsSerde
import mu.KotlinLogging
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.Topology
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.KStream
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.charset.Charset

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
        val prefilters = mutableListOf<PDnsPrefilter>()
        appProperties.prefilters?.forEach {
            prefilters.add(PDnsPrefilter(it.field, it.pattern, it.allow))
        }
        val builder = StreamsBuilder()
        var src = builder.stream(
                appProperties.input.path,
                Consumed.with(Serdes.String(), PDnsSerde())
        )
        prefilters.forEach {
            src = src.filter(it)
        }
        val whitelistPredicate = WhitelistPredicate()
        appProperties.whitelists.forEach {
            when (it.type) {
                "file" -> whitelistPredicate.fromFile(it.path)
                "regex" -> whitelistPredicate.fromRegex(it.path)
                else -> throw Exception("Invalid whitelist source type <${it.type}>")
            }
        }
        val matchRecords = src
                .filter { _, v -> whitelistPredicate.test(v.topPrivateDomain) }
        val missRecords = src
                .filterNot { _, v -> whitelistPredicate.test(v.topPrivateDomain) }

        appProperties.output.match.forEach {
            configSinks(matchRecords, it.type, it.path, it.options)
        }
        appProperties.output.miss.forEach {
            configSinks(missRecords, it.type, it.path, it.options)
        }
        return builder.build()
    }

    fun configSinks(src: KStream<String, PDnsData>, type: String, path: String, options: Map<String, String>) {
        var sinkSrc = src
        val unique = options.getOrDefault("unique", "false").toBoolean()
        if (unique) {
            sinkSrc = src.filterNot(BloomFilterKStreamPredicate(
                    Funnels.stringFunnel(Charset.defaultCharset()),
                    options.getOrDefault("expectedInsertions", "2000000000").toLong(),
                    options.getOrDefault("fpp", "0.01").toDouble()
            ))
        }
        when (type) {
            "topic" -> sinkSrc.to(path)
            "file" -> sinkSrc.foreach { _, v ->
                fileSinkWriters.get(path)!!.println(
                        "${v.queryTime},${v.queryType},${v.domain},${v.topPrivateDomain},${v.replyCode},${v.ips?.joinToString(";")},${v.cnames?.joinToString(";")}"
                )
            }
            else -> throw Exception("Invalid sink type <$type>")
        }
    }
}