package com.hitnslab.dnssecurity.pdnsdataloader.config

import com.hitnslab.dnssecurity.pdnsdataloader.config.job.FileHashJobConfig
import com.hitnslab.dnssecurity.pdnsdataloader.config.job.LoadPDNSDataJobConfig
import mu.KotlinLogging
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.core.configuration.support.ApplicationContextFactory
import org.springframework.batch.core.configuration.support.GenericApplicationContextFactory
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.SimpleJobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor


@Configuration
@EnableBatchProcessing(modular = true)
class SpringBatchConfig : DefaultBatchConfigurer() {

    private val logger = KotlinLogging.logger {}

    @Autowired
    private lateinit var taskExecutor: TaskExecutor

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
        jobLauncher.setTaskExecutor(taskExecutor)
        jobLauncher.afterPropertiesSet()
        return jobLauncher
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