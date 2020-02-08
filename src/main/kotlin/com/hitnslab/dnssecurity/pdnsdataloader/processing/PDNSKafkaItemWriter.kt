package com.hitnslab.dnssecurity.pdnsdataloader.processing

import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsData
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.batch.item.ItemWriter
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback
import java.util.concurrent.atomic.AtomicInteger

class PDNSKafkaItemWriter(val kafkaTemplate: KafkaTemplate<String, PDnsData>) : ItemWriter<PDnsData> {

    private val logger = KotlinLogging.logger {}

    var sentCount = AtomicInteger(0)

    var failedCount = AtomicInteger(0)

    var successInterval = 100000

    var failureInterval = 100000

    private val itemCallback = object : ListenableFutureCallback<SendResult<*, *>> {
        override fun onSuccess(result: SendResult<*, *>?) {
            val current = sentCount.incrementAndGet()
            if (current >= successInterval) {
                logger.info { "Successfully wrote $current items to kafka" }
                sentCount.addAndGet(-current)
            }
        }

        override fun onFailure(ex: Throwable) {
            val current = failedCount.incrementAndGet()
            if (current % failureInterval == 0) {
                logger.error { "Failed to send $current items with exception <$ex>" }
            }
        }
    }

    override fun write(items: MutableList<out PDnsData>) {
        val futures = items
                .forEach {
                    //            val headers = mutableListOf<Header>()
                    val record = ProducerRecord<String, PDnsData>(kafkaTemplate.defaultTopic, null, it.queryTime.epochSecond, it.domain, it)
                    val future = kafkaTemplate.send(record)
                    future.addCallback(itemCallback)
                }

    }
}