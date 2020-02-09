package com.hitnslab.dnssecurity.pdnsdataloader.batch.execute

import mu.KotlinLogging
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.metrics.BatchMetrics
import org.springframework.batch.core.repository.JobRepository
import org.springframework.core.task.TaskExecutor
import org.springframework.core.task.TaskRejectedException

open class DefaultJobExecutor(val jobRepository: JobRepository, val taskExecutor: TaskExecutor) : JobExecutor {

    private val logger = KotlinLogging.logger {}

    override fun execute(job: Job, jobExecution: JobExecution) {
        try {
            taskExecutor.execute(object : Runnable {
                override fun run() {
                    try {
                        logger.info("Job: [" + job + "] launched with the following parameters: [" + jobExecution.jobParameters
                                + "]")
                        job.execute(jobExecution)
                        val jobExecutionDuration = BatchMetrics.calculateDuration(jobExecution.startTime, jobExecution.endTime)
                        logger.info("Job: [" + job + "] completed with the following parameters: [" + jobExecution.jobParameters
                                + "] and the following status: [" + jobExecution.status + "]"
                                + if (jobExecutionDuration == null) "" else " in " + BatchMetrics.formatDuration(jobExecutionDuration))
                    } catch (t: Throwable) {
                        logger.info("Job: [" + job
                                + "] failed unexpectedly and fatally with the following parameters: [" + jobExecution.jobParameters
                                + "]", t)
                        rethrow(t)
                    }
                }

                private fun rethrow(t: Throwable) {
                    if (t is RuntimeException) {
                        throw t
                    } else if (t is Error) {
                        throw t
                    }
                    throw IllegalStateException(t)
                }
            })
        } catch (e: TaskRejectedException) {
            jobExecution.upgradeStatus(BatchStatus.FAILED)
            if (jobExecution.exitStatus.equals(ExitStatus.UNKNOWN)) {
                jobExecution.exitStatus = ExitStatus.FAILED.addExitDescription(e)
            }
            jobRepository.update(jobExecution)
        }
    }
}