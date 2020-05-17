package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.batch.execute

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution

/**
 * Interface for executing a Job with a JobExecution instance.
 *
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
fun interface JobExecutor {
    fun execute(job: Job, jobExecution: JobExecution)
}