package com.hitnslab.dnssecurity.deeparcher.stream.config

import com.github.benmanes.caffeine.cache.LoadingCache
import com.hitnslab.dnssecurity.deeparcher.stream.property.AppProperties
import mu.KotlinLogging
import org.apache.kafka.streams.KafkaStreams
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafkaStreams
import org.springframework.kafka.config.StreamsBuilderFactoryBeanCustomizer

@Configuration
@EnableKafkaStreams
@EnableConfigurationProperties(AppProperties::class)
class KafkaStreamConfig {

    @Autowired
    lateinit var appProperties: AppProperties

    @Autowired
    lateinit var caches: Collection<LoadingCache<*, *>>

    private val logger = KotlinLogging.logger {}

    @Bean
    fun customizer(): StreamsBuilderFactoryBeanCustomizer {
        return StreamsBuilderFactoryBeanCustomizer { fb ->
            fb.setStateListener { newState, _ ->
                if (newState == KafkaStreams.State.NOT_RUNNING) {
                    caches.forEach {
                        logger.info { "Destroying <$it>..." }
                        it.invalidateAll()
                        logger.info { "Destroyed <$it>" }
                    }
                }
            }
        }
    }


}