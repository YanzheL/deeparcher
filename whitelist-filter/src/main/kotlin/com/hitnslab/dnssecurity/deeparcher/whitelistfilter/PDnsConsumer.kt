package com.hitnslab.dnssecurity.deeparcher.whitelistfilter

import com.github.benmanes.caffeine.cache.Caffeine
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import java.util.concurrent.TimeUnit

@Service
class PDnsConsumer {

    val predicate = WhiteListPredicate("whitelist.csv")
    val writer = PrintWriter(FileWriter(File("filtered.txt"), true), true)
    val cache = Caffeine.newBuilder()
            .maximumSize(100000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build<String, String>()
    val rtypes = setOf("A", "AAAA")

    @KafkaListener(topics = ["#{'\${app.kafka.input.topic}'}"])
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