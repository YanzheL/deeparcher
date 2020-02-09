package com.hitnslab.dnssecurity.pdnsdataloader.config

import com.hitnslab.dnssecurity.pdnsdataloader.config.job.FileHashJobConfig
import com.hitnslab.dnssecurity.pdnsdataloader.config.job.LoadPDNSDataJobConfig
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serializer
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.support.ApplicationContextFactory
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import java.util.function.Supplier


@Configuration
@EnableBatchProcessing(modular = true)
class SpringBatchConfig : DefaultBatchConfigurer() {

    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var applicationTaskExecutor: TaskExecutor

    @Bean
    fun job1(): ApplicationContextFactory? {
        return GenericApplicationContextFactory(LoadPDNSDataJobConfig::class.java)
    }

    @Bean
    fun job2(): ApplicationContextFactory? {
        return GenericApplicationContextFactory(FileHashJobConfig::class.java)
    }

    override fun createJobLauncher(): JobLauncher {
        val jobLauncher = SimpleJobLauncher()
        jobLauncher.setJobRepository(jobRepository)
        jobLauncher.setTaskExecutor(applicationTaskExecutor)
        jobLauncher.afterPropertiesSet()
        return jobLauncher
    }

    @Bean
    @ConditionalOnClass(EnableKafka::class)
    fun kafkaProducerFactory(properties: KafkaProperties): ProducerFactory<*, *>? {
        val producerConfig = properties.buildProducerProperties()
        producerConfig.remove(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG)
        producerConfig.remove(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG)
        val keySupplier = Supplier { properties.producer.keySerializer.getConstructor().newInstance() as Serializer<Any> }
        val valueSupplier = Supplier { properties.producer.valueSerializer.getConstructor().newInstance() as Serializer<Any> }
        val factory = DefaultKafkaProducerFactory<Any, Any>(
                producerConfig,
                keySupplier,
                valueSupplier
        )
        val transactionIdPrefix: String? = properties.producer.transactionIdPrefix
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix)
        }
//        factory.setProducerPerThread(true)
        return factory
    }

//    fun jobTaskExecutor(): TaskExecutor {
//        val taskExecutor = ThreadPoolTaskExecutor()
////        taskExecutor.maxPoolSize = 12
//        taskExecutor.corePoolSize = Runtime.getRuntime().availableProcessors()
////        taskExecutor.setAllowCoreThreadTimeOut(true)
//        taskExecutor.afterPropertiesSet()
//        return taskExecutor
//    }
}