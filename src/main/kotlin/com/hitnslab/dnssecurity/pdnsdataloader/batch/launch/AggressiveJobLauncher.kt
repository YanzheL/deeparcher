package com.hitnslab.dnssecurity.pdnsdataloader.batch.launch

import com.hitnslab.dnssecurity.pdnsdataloader.batch.execute.JobExecutor
import mu.KotlinLogging
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.JobRestartException
import java.util.*

open class AggressiveJobLauncher(val jobRepository: JobRepository) : DeferredJobLauncher {

    var jobExecutor: JobExecutor? = null

    var launched: Boolean = false

    private val logger = KotlinLogging.logger {}

    override fun isLaunched(): Boolean {
        return launched
    }

    override fun run(job: Job, jobParameters: JobParameters): JobExecution {
        val lastExecution: JobExecution? = jobRepository.getLastJobExecution(job.name, jobParameters)
        val resetStatuses = setOf(
                BatchStatus.STARTING,
                BatchStatus.STARTED,
                BatchStatus.STOPPING,
                BatchStatus.UNKNOWN
        )
        if (null != lastExecution) {
            logger.info {
                "Found an existing job execution <$lastExecution>, checking whether it is restartable..."
            }
            if (lastExecution.status == BatchStatus.COMPLETED) {
                logger.info {
                    "The last job execution is <${lastExecution.status}> for parameters=<$jobParameters>. If you want to run this job again, change the parameters."
                }
                return lastExecution
            }
            if (!job.isRestartable) {
                throw JobRestartException("JobInstance already exists and is not restartable")
            }
            val currentTime = Date()
            if (lastExecution.status in resetStatuses) {
                logger.info {
                    "The last job execution is <${lastExecution.status}> for parameters=<$jobParameters>, resetting it to STOPPED"
                }
                lastExecution.status = BatchStatus.STOPPED
                lastExecution.endTime = currentTime
            }
            for (execution in lastExecution.stepExecutions) {
                if (execution.status in resetStatuses) {
                    logger.info {
                        "A step execution <$execution> is <${execution.status}>, resetting it to STOPPED"
                    }
                    execution.status = BatchStatus.STOPPED
                    execution.endTime = currentTime
                    jobRepository.update(execution)
                }
            }
            jobRepository.update(lastExecution)
        }
        // Check the validity of the parameters before doing creating anything in the repository...
        job.jobParametersValidator.validate(jobParameters)

        val jobExecution: JobExecution = jobRepository.createJobExecution(job.name, jobParameters)

        val executor = jobExecutor
        if (executor != null) {
            launched = true
            executor.execute(job, jobExecution)
        }
        return jobExecution
    }
}