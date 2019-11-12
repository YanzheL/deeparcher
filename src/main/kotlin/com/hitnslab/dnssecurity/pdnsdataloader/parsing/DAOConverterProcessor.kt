package com.hitnslab.dnssecurity.pdnsdataloader.parsing

import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsData
import com.hitnslab.dnssecurity.pdnsdataloader.model.PDnsDataDAO
import mu.KotlinLogging
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class DAOConverterProcessor : ItemProcessor<PDnsData, PDnsDataDAO?> {

    private val logger = KotlinLogging.logger {}

    override fun process(item: PDnsData): PDnsDataDAO? {
        return try {
            PDnsDataDAO(item)
        } catch (e: Exception) {
            logger.warn { "Unacceptable PDnsData<$item>" }
            null
        }
    }
}