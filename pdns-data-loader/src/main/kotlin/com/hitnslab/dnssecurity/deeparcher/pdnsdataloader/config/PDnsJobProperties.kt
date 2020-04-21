package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.config

import com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.parsing.PDNSLogFieldSetMapper
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.transaction.PlatformTransactionManager

@ConstructorBinding
@ConfigurationProperties(prefix = "app.job.pdns")
class PDnsJobProperties(
    val step: Step,
    val tryRestart: Boolean = true
) {

    class Step(
        val itemReader: ItemReader,

        val itemWriter: ItemWriter,

        val chunkSize: Int = 10000,

        val retryLimit: Int = 10,

        val transactionManager: Class<PlatformTransactionManager>? = null
    ) {

        class ItemReader(
            val fieldSetMapper: Class<PDNSLogFieldSetMapper>
        )

        class ItemWriter(
            val name: String,
            val metrics: Metrics = Metrics()
        ) {
            class Metrics(
                val enable: Boolean = true,
                val successInterval: Int = 1000000,
                val failureInterval: Int = 10000
            )
        }

    }
}