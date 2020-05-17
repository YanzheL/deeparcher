package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.batch

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.PDnsDataProto.PDnsData
import mu.KotlinLogging
import org.springframework.batch.item.ItemWriter
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback
import java.util.concurrent.atomic.AtomicInteger

/**
 * Write PDnsData protobuf object to kafka.
 *
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class PDNSKafkaItemWriter(val kafkaTemplate: KafkaTemplate<String, PDnsData>) : ItemWriter<PDnsData> {

    private val logger = KotlinLogging.logger {}

    var sentCount = AtomicInteger(0)

    var failedCount = AtomicInteger(0)

    var metricsEnabled = false

    var successInterval = 100000

    var failureInterval = 10000

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
        items.forEach {
            //            val headers = mutableListOf<Header>()
//            val record = ProducerRecord(kafkaTemplate.defaultTopic, null, it.queryTime.toEpochMilli(), it.domain, it)
//            val future = kafkaTemplate.send(record)
            val future = kafkaTemplate.send(kafkaTemplate.defaultTopic, it.domain, it)
            if (metricsEnabled) {
                future.addCallback(itemCallback)
            }
        }
//        logger.info { "ItemWriter wrote ${items.size} items" }
    }
}