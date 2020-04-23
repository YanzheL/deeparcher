package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader

import mu.KotlinLogging
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.JobParametersBuilder
import org.springframework.batch.core.configuration.JobRegistry
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.batch.JobExecutionEvent
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.ApplicationEventPublisherAware
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.util.*

@Component
class CustomCommandLineRunner : CommandLineRunner, ApplicationEventPublisherAware {

    @Autowired
    lateinit var jobLauncher: JobLauncher

    @Autowired
    lateinit var jobRegistry: JobRegistry

    @Autowired
    lateinit var applicationTaskExecutor: ThreadPoolTaskExecutor

    @Autowired(required = false)
    var publisher: ApplicationEventPublisher? = null

    private val logger = KotlinLogging.logger {}

    override fun setApplicationEventPublisher(publisher: ApplicationEventPublisher) {
        this.publisher = publisher
    }

    override fun run(vararg args: String) {
        logger.info("Running default command line with: " + listOf(*args))
        val job = jobRegistry.getJob("FILE_HASH")
        val properties = StringUtils.splitArrayElementsIntoProperties(args, "=")
        buildJobParameters(properties)
            .map { execute(job, it) }
//        applicationTaskExecutor.setWaitForTasksToCompleteOnShutdown(true)
//        applicationTaskExecutor.shutdown()
    }

    protected fun buildJobParameters(properties: Properties?): List<JobParameters> {
        val pattern = properties!!["pattern"].toString()
        val resources = PathMatchingResourcePatternResolver().getResources(pattern)
        resources.sortBy(Resource::getURI)
        logger.debug { "Discovered all log file resources as follows:\n$resources" }
        val allParameters = mutableListOf<JobParameters>()
        for (resource in resources) {
            val path = resource.file.path
            val parameters = JobParametersBuilder()
                .addString("file", path)
                .addDate("createdAt", Date())
                .toJobParameters()
            allParameters.add(parameters)
        }
        return allParameters
    }

    protected fun execute(job: Job, jobParameters: JobParameters): JobExecution {
        val execution = jobLauncher.run(job, jobParameters)
        logger.debug { "Created parent job execution <$execution>" }
        publisher?.publishEvent(JobExecutionEvent(execution))
        return execution
    }
}