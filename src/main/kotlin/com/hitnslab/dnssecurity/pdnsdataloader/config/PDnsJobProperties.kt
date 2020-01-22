package com.hitnslab.dnssecurity.pdnsdataloader.config

import com.hitnslab.dnssecurity.pdnsdataloader.parsing.PDNSLogFieldSetMapper
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties(prefix = "app.job")
@Component
class PDnsJobProperties {

    var slaveStep: SlaveStep? = null

    class SlaveStep {

        var itemReader: ItemReader? = null

        var itemWriter: ItemWriter? = null

        var chunkSize: Int = 100000

        var retryLimit: Int = 10

        class ItemReader {
            lateinit var fieldSetMapper: Class<PDNSLogFieldSetMapper>
        }

        class ItemWriter {
            lateinit var name: String
        }

    }
}