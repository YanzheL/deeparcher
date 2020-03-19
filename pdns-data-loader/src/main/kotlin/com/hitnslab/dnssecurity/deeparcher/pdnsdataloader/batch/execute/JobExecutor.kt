package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.batch.execute

import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution

interface JobExecutor {
    fun execute(job: Job, jobExecution: JobExecution)
}