package com.hitnslab.dnssecurity.pdnsdataloader.batch.launch

import org.springframework.batch.core.launch.JobLauncher

interface DeferredJobLauncher : JobLauncher {
    fun isLaunched(): Boolean
}