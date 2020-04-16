package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import mu.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.File
import java.io.FileWriter
import java.io.PrintWriter

@Configuration
class CacheConfig {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun fileWriterCache(): LoadingCache<String, PrintWriter> {
        return Caffeine.newBuilder()
                .maximumSize(100)
                .removalListener<String, PrintWriter> { _, v, cause ->
                    v?.close()
                    logger.info { "Writer-Cache evicted <$v> for cause <$cause>" }
                }
                .build {
                    PrintWriter(FileWriter(File(it), true), true)
                }
    }
}