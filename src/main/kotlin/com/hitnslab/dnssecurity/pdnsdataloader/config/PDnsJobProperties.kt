package com.hitnslab.dnssecurity.pdnsdataloader.config

import com.hitnslab.dnssecurity.pdnsdataloader.parsing.PDNSLogFieldSetMapper
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager

@ConfigurationProperties(prefix = "app.job.pdns")
@Component
class PDnsJobProperties {

    var step: Step = Step()

    class Step {

        var itemReader: ItemReader = ItemReader()

        var itemWriter: ItemWriter = ItemWriter()

        var transaction: Transaction = Transaction()

        var chunkSize: Int = 10000

        var retryLimit: Int = 10


        class ItemReader {
            lateinit var fieldSetMapper: Class<PDNSLogFieldSetMapper>
        }

        class ItemWriter {
            lateinit var name: String
        }

        class Transaction {
            var enable: Boolean = false
            var manager: Class<PlatformTransactionManager>? = null
        }

    }
}