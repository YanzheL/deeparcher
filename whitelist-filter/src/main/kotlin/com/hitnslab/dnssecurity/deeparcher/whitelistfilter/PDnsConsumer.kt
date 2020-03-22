package com.hitnslab.dnssecurity.deeparcher.whitelistfilter

import com.github.benmanes.caffeine.cache.Caffeine
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.KafkaListener
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.concurrent.TimeUnit

@Configuration
class PDnsConsumer {

    @Autowired
    lateinit var appProperties: AppProperties

    val rtypes = setOf("A", "AAAA")

    val predicate by lazy { WhitelistPredicate(appProperties.whitelist.file) }

    val writer by lazy { PrintWriter(FileWriter(File(appProperties.output.file), true), true) }

    val cache = Caffeine.newBuilder()
            .maximumSize(100000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build<String, String>()

    @KafkaListener(topics = ["#{'\${app.input.topic}'}"])
    fun process(data: PDnsData) {
        if (data.queryType !in rtypes) {
            return
        }
        if (predicate.test(data)) {
            return
        }
        if (cache.getIfPresent(data.domain) == null) {
            writer.println(data.domain)
            cache.put(data.domain, data.domain)
        }
    }
}