package com.hitnslab.dnssecurity.pdnsdataloader.processing

import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsData
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.batch.item.ItemWriter
import org.springframework.kafka.core.KafkaTemplate

class PDNSKafkaItemWriter(val kafkaTemplate: KafkaTemplate<String, PDnsData>) : ItemWriter<PDnsData> {

    private val logger = KotlinLogging.logger {}

    override fun write(items: MutableList<out PDnsData>) {
        val results = items
                .map {
                    //            val headers = mutableListOf<Header>()
                    val record = ProducerRecord<String, PDnsData>(kafkaTemplate.defaultTopic, null, it.queryTime.epochSecond, it.domain, it)
                    kafkaTemplate.send(record)
                }
                .asSequence()
                .map {
                    val sendResult = it.get()
                    if (!it.isDone) {
                        logger.error { "Failed to send <${sendResult.producerRecord.value()}> to Kafka" }
                    }
                    it
                }
                .filterNot { it.isDone }
                .toList()
        if (results.isEmpty()) {
            logger.info { "Successfully wrote ${items.size} items to kafka" }
        } else {
            logger.warn { "Fail to wrote ${results.size} items to kafka" }
        }
    }
}