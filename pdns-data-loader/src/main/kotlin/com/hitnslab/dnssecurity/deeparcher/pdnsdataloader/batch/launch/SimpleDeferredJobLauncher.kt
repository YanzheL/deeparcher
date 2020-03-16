package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.batch.launch

import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.Job
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.repository.JobRestartException

open class SimpleDeferredJobLauncher(val jobRepository: JobRepository) : DeferredJobLauncher {

    override fun isLaunched(): Boolean {
        return false
    }

    override fun run(job: Job, jobParameters: JobParameters): JobExecution {
        val lastExecution: JobExecution? = jobRepository.getLastJobExecution(job.name, jobParameters)
        if (null != lastExecution) {
            if (!job.isRestartable) {
                throw JobRestartException("JobInstance already exists and is not restartable")
            }
            /*
			 * validate here if it has stepExecutions that are UNKNOWN, STARTING, STARTED and STOPPING
			 * retrieve the previous execution and check
			 */
            for (execution in lastExecution.stepExecutions) {
                val status = execution.status
                if (status.isRunning || status == BatchStatus.STOPPING) {
                    throw JobExecutionAlreadyRunningException("A job execution for this job is already running: "
                            + lastExecution)
                } else if (status == BatchStatus.UNKNOWN) {
                    throw JobRestartException(
                            "Cannot restart step [" + execution.stepName + "] from UNKNOWN status. "
                                    + "The last execution ended with a failure that could not be rolled back, "
                                    + "so it may be dangerous to proceed. Manual intervention is probably necessary.")
                }
            }
        }
        // Check the validity of the parameters before doing creating anything in the repository...
        job.jobParametersValidator.validate(jobParameters)
        /*
		 * There is a very small probability that a non-restartable job can be
		 * restarted, but only if another process or thread manages to launch
		 * <i>and</i> fail a job execution for this instance between the last
		 * assertion and the next method returning successfully.
		 */

        val jobExecution: JobExecution = jobRepository.createJobExecution(job.name, jobParameters)

        return jobExecution
    }
}