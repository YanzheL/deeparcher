package com.hitnslab.dnssecurity.deeparcher.stream.service

import com.github.benmanes.caffeine.cache.Caffeine
import mu.KotlinLogging
import org.bson.Document
import org.springframework.data.mongodb.BulkOperationException
import org.springframework.data.mongodb.core.BulkOperations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.aggregation.Aggregation.group
import org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation
import org.springframework.data.mongodb.core.query.Criteria.where
import org.springframework.data.mongodb.core.query.Query.query
import org.springframework.data.mongodb.core.query.Update.update
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicLong


class MongoStringIdService(
    val template: MongoTemplate,
    val collection: String,
    val keyField: String,
    val valueField: String
) : ObjectIdService<String> {

    private val logger = KotlinLogging.logger {}

    private val cacheLimit: Long = 1000000L

    private val maxId = AtomicLong(-1)

    private val cache = Caffeine.newBuilder()
        .maximumSize(cacheLimit)
        .removalListener<String, Long> { key, value, _ ->
            uncommitedEntries.add(
                AbstractMap.SimpleEntry(
                    key!!,
                    value!!
                )
            )
        }
        .build<String, Long>(::generateId)

    private var initialized = false

    private val uncommitedEntries = ConcurrentLinkedQueue<Map.Entry<String, Long>>()

    override fun getCurrentMaxId(): Long {
        ensureInitialized()
        val cur = maxId.get()
        return if (cur >= 0) {
            cur
        } else {
            getCurrentMaxIdRemote()
        }
    }

    override fun getOrCreateId(key: String): Long {
        ensureInitialized()
        return cache.get(key)!!
    }

    override fun getExistingId(key: String): Long? {
        ensureInitialized()
        return cache.getIfPresent(key) ?: getExistingIdRemote(key)
    }

    override fun getAllExistingEntries(): Sequence<Map.Entry<String, Long>> {
        val query = query(where(keyField).exists(true).and(valueField).exists(true))
        query.fields().include(keyField).include(valueField).exclude("_id")
        return template.find(
            query,
            Document::class.java
        ).map { AbstractMap.SimpleEntry(it.getString(keyField), it.getLong(valueField)) }.asSequence()
    }

    override fun commit(): Boolean {
        if (uncommitedEntries.isEmpty()) {
            return true
        }
        val ops = template.bulkOps(BulkOperations.BulkMode.UNORDERED, collection)
        val batch = mutableListOf<Map.Entry<String, Long>>()
        while (uncommitedEntries.isNotEmpty()) {
            val entry = uncommitedEntries.poll()
            batch.add(entry)
            ops.upsert(
                query(where(keyField).`is`(entry.key)),
                update(valueField, entry.value)
            )
        }
        return try {
            val result = ops.execute()
            logger.info { "Committed ${result.upserts.size} graph node ids to MongoDB." }
            true
        } catch (e: BulkOperationException) {
            val succeed = e.result.upserts.map { it.index }.map { batch[it] }
            batch.removeAll(succeed)
            logger.error {
                "Cannot commit graph node ids to MongoDB, " +
                        "result = <${e.result}>, exception = <${e}>, errors = <${e.errors}>. " +
                        "${batch.size} unsuccessful upserts will be retried later."
            }
            uncommitedEntries.addAll(batch)
            false
        }
    }

    override fun close() {
        var maxRetry = 30
        while (!commit() && uncommitedEntries.isNotEmpty() && --maxRetry > 0) {
            Thread.sleep(1000)
        }
    }

    private fun generateId(key: String): Long {
        ensureInitialized()
        val id = maxId.incrementAndGet()
        uncommitedEntries.add(AbstractMap.SimpleEntry(key, id))
        return id
    }

    private fun getCurrentMaxIdRemote(): Long {
        val agg = newAggregation(group().max(valueField).`as`("max"))
        return template.aggregate(agg, collection, Document::class.java).uniqueMappedResult?.getLong("max") ?: -1
    }

    private fun getExistingIdRemote(key: String): Long? {
        return template.findOne(
            query(where(keyField).`is`(key).and(valueField).exists(true)),
            Document::class.java
        )?.getLong(valueField)
    }

    private fun ensureInitialized() {
        // TODO: This is not thread-safe.
        if (!initialized) {
            maxId.set(getCurrentMaxIdRemote())
            getAllExistingEntries().forEach { cache.put(it.key, it.value) }
            initialized = true
        }
    }
}