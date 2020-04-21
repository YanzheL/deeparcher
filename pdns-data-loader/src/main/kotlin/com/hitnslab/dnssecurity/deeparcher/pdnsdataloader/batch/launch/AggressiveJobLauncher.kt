package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.batch.launch

import com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.batch.execute.JobExecutor
import mu.KotlinLogging
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.repository.JobRepository
import java.util.*

open class AggressiveJobLauncher(val jobRepository: JobRepository) : DeferredJobLauncher {

    var jobExecutor: JobExecutor? = null

    var launched = false

    var tryRestart = true

    private val logger = KotlinLogging.logger {}

    override fun isLaunched(): Boolean {
        return launched
    }

    override fun run(job: Job, jobParameters: JobParameters): JobExecution {
        logger.info { "Searching for last job execution of <${job.name}> with parameters <$jobParameters> " }
        val lastExecution: JobExecution? = jobRepository.getLastJobExecution(job.name, jobParameters)
        lastExecution?.let { le ->
            logger.info {
                "A job execution <$le> already exists, checking whether it is restartable..."
            }
            if (!tryRestart) {
                logger.info { "This instance of AggressiveJobLauncher is configured as 'tryRestart = false', so the job <${job.name}> with parameters <$jobParameters> is skipped and maintained previous status <${le.status}>" }
                return le
            }
            val resettableStatuses = setOf(
                BatchStatus.STARTING,
                BatchStatus.STARTED,
                BatchStatus.STOPPING,
                BatchStatus.UNKNOWN
            )
            if (le.status == BatchStatus.COMPLETED) {
                logger.info {
                    "The last job execution is <${le.status}> for parameters <$jobParameters>. If you want to run this job again, change the parameters."
                }
                return le
            }
            if (!job.isRestartable) {
                logger.info { "JobExecution <$le> already exists and <${job.name}> is not restartable" }
                return le
            }
            val currentTime = Date()
            if (le.status in resettableStatuses) {
                logger.info {
                    "The last job execution is <${le.status}> for parameters <$jobParameters>, resetting it to STOPPED"
                }
                le.status = BatchStatus.STOPPED
                le.endTime = currentTime
            }
            for (se in le.stepExecutions) {
                if (se.status in resettableStatuses) {
                    logger.info {
                        "A step execution <$se> is <${se.status}>, resetting it to STOPPED"
                    }
                    se.status = BatchStatus.STOPPED
                    se.endTime = currentTime
                    jobRepository.update(se)
                }
            }
            jobRepository.update(le)
        }
        // Check the validity of the parameters before doing creating anything in the repository...
        job.jobParametersValidator.validate(jobParameters)

        val jobExecution: JobExecution = jobRepository.createJobExecution(job.name, jobParameters)

        jobExecutor?.let {
            it.execute(job, jobExecution)
            launched = true
        }
        return jobExecution
    }
}