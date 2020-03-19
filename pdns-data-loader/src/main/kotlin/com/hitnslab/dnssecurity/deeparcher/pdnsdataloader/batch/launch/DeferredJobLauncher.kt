package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.batch.launch

import org.springframework.batch.core.launch.JobLauncher

interface DeferredJobLauncher : JobLauncher {
    fun isLaunched(): Boolean
}