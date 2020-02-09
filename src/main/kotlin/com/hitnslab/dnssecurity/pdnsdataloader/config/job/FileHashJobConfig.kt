package com.hitnslab.dnssecurity.pdnsdataloader.config.job

import com.hitnslab.dnssecurity.pdnsdataloader.batch.FileHashTasklet
import com.hitnslab.dnssecurity.pdnsdataloader.batch.execute.DefaultJobExecutor
import com.hitnslab.dnssecurity.pdnsdataloader.batch.launch.AggressiveJobLauncher
import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.listener.ExecutionContextPromotionListener
import org.springframework.batch.core.repository.JobRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.core.task.TaskExecutor

class FileHashJobConfig {

    private val logger = KotlinLogging.logger {}

    @Autowired
    lateinit var jobRepository: JobRepository

    @Autowired
    lateinit var jobBuilderFactory: JobBuilderFactory

    @Autowired
    lateinit var stepBuilderFactory: StepBuilderFactory

    @Autowired
    lateinit var jobRegistry: JobRegistry

    @Bean
    fun job(steps: List<Step>): Job {
        var builder = jobBuilderFactory.get("FILE_HASH")
                .repository(jobRepository)
                .start(steps[0])
        if (steps.size >= 2) {
            for (i in 1 until steps.size) {
                builder = builder.next(steps[i])
            }
        }
        return builder.build()
    }

    @Bean
    fun step0(executionContextPromotionListener: ExecutionContextPromotionListener): Step {
        return stepBuilderFactory.get("FILE_HASH_STEP0")
                .tasklet(FileHashTasklet())
                .listener(executionContextPromotionListener)
                .build()
    }

    @Bean
    fun step1(aggressiveJobLauncher: AggressiveJobLauncher): Step {
        val targetJob = jobRegistry.getJob("LOAD_PDNS_DATA")
        return stepBuilderFactory.get("FILE_HASH_STEP1")
                .job(targetJob)
                .parametersExtractor { _, stepExecution ->
                    JobParametersBuilder()
                            .addString("hash", stepExecution.jobExecution.executionContext["hash"] as String)
                            .addString("file", stepExecution.jobParameters.getString("file")!!, false)
                            .toJobParameters()
                }
                .launcher(aggressiveJobLauncher)
                .build()
    }

    @Bean
    fun aggressiveJobLauncher(jobStepTaskExecutor: TaskExecutor): AggressiveJobLauncher {
        val launcher = AggressiveJobLauncher(jobRepository)
        launcher.jobExecutor = DefaultJobExecutor(jobRepository, jobStepTaskExecutor)
        return launcher
    }

//    @Bean
//    fun jobStepTaskExecutor(taskExecutorBuilder: TaskExecutorBuilder): TaskExecutor {
//        val executor = taskExecutorBuilder
//                .threadNamePrefix("job-step-")
//                .build()
//        executor.initialize()
//        return executor
//    }

    @Bean
    fun promotionListener(): ExecutionContextPromotionListener {
        val listener = ExecutionContextPromotionListener()
        listener.setKeys(arrayOf("hash"))
        return listener
    }
}