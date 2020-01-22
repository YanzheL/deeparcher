package com.hitnslab.dnssecurity.pdnsdataloader.batch

import mu.KotlinLogging
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.context.annotation.Configuration


@Configuration
@EnableBatchProcessing
class SpringBatchConfig : DefaultBatchConfigurer() {

    private val logger = KotlinLogging.logger {}

//    @Primary
//    @Bean
//    fun taskExecutor(): ThreadPoolTaskExecutor {
//        val taskExecutor = ThreadPoolTaskExecutor()
////        taskExecutor.maxPoolSize = 12
//        taskExecutor.corePoolSize = Runtime.getRuntime().availableProcessors()
////        taskExecutor.setAllowCoreThreadTimeOut(true)
//        taskExecutor.afterPropertiesSet()
//        return taskExecutor
//    }

}