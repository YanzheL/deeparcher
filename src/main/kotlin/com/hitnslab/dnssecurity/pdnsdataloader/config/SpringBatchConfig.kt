package com.hitnslab.dnssecurity.pdnsdataloader.config

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor


@Configuration
class SpringBatchConfig : DefaultBatchConfigurer() {

    @Bean
    fun threadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        val taskExecutor = ThreadPoolTaskExecutor()
//        taskExecutor.maxPoolSize = 12
        taskExecutor.corePoolSize = Runtime.getRuntime().availableProcessors()
//        taskExecutor.setAllowCoreThreadTimeOut(true)
        taskExecutor.afterPropertiesSet()
        return taskExecutor
    }
}