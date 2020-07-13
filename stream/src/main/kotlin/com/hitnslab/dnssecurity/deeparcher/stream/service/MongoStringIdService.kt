package com.hitnslab.dnssecurity.deeparcher.stream.service

import com.github.benmanes.caffeine.cache.Caffeine
import com.mongodb.MongoBulkWriteException
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Accumulators.max
import com.mongodb.client.model.Aggregates.group
import com.mongodb.client.model.BulkWriteOptions
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Projections.include
import com.mongodb.client.model.UpdateOneModel
import com.mongodb.client.model.UpdateOptions
import com.mongodb.client.model.Updates.*
import com.mongodb.client.model.WriteModel
import mu.KotlinLogging
import org.bson.Document
import org.bson.conversions.Bson
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicLong
import java.util.function.Function

class MongoStringIdService(
    database: String,
    collection: String,
    client: MongoClient,
    private val keyField: String,
    private val valueField: String,
    private val setOnInsertProvider: Function<Map.Entry<String, Long>, Iterable<Bson>>? = null
) : ObjectIdService<String> {

    private val logger = KotlinLogging.logger {}

    private val maxId = AtomicLong(-1)

    private val db: MongoDatabase

    private val col: MongoCollection<Document>

    private var initialized = false

    private val uncommitedEntries: Queue<Map.Entry<String, Long>>

    private val cache = Caffeine.newBuilder()
        .build<String, Long>(::generateId)


    init {
        db = client.getDatabase(database)
        col = db.getCollection(collection)
        uncommitedEntries = ConcurrentLinkedQueue()
    }

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
        return col.find(
            and(
                exists(keyField),
                exists(valueField)
            )
        )
            .projection(
                include(keyField, valueField)
            )
            .map { AbstractMap.SimpleEntry(it.getString(keyField), it.getLong(valueField)) }
            .asSequence()
    }

    override fun commit(): Boolean {
        if (uncommitedEntries.isEmpty()) {
            return true
        }
        val bulk = mutableListOf<WriteModel<Document>>()
        val batch = mutableListOf<Map.Entry<String, Long>>()
        while (uncommitedEntries.isNotEmpty()) {
            val entry = uncommitedEntries.poll()
            batch.add(entry)
            val setOnInserts = setOnInsertProvider
                ?.apply(entry)
                ?.map { setOnInsert(it) }?.toTypedArray() ?: emptyArray()
            bulk.add(
                UpdateOneModel(
                    eq(keyField, entry.key),
                    combine(
                        set(valueField, entry.value),
                        set("updated_at", Date()),
                        *setOnInserts
                    ),
                    UpdateOptions().upsert(true)
                ),
            )
        }
        return try {
            val result = col.bulkWrite(bulk, BulkWriteOptions().ordered(false))
            logger.info { "Committed ${result.upserts.size} graph node ids to MongoDB." }
            true
        } catch (e: MongoBulkWriteException) {
            val succeed = e.writeResult.upserts.map { it.index }.map { batch[it] }
            batch.removeAll(succeed)
            logger.error {
                "Cannot commit graph node ids to MongoDB, " +
                        "result = <${e.writeResult}>, exception = <${e}>, errors = <${e.writeErrors}>. " +
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
        return col.aggregate(
            listOf(
                group(null, max("max", "\$${valueField}"))
            )
        ).first()?.getLong("max") ?: -1
    }

    private fun getExistingIdRemote(key: String): Long? {
        return col.find(
            and(
                eq(keyField, key),
                exists(valueField)
            )
        ).first()?.getLong(valueField)
    }

    private fun ensureInitialized() {
        // TODO: This is not thread-safe.
        if (!initialized) {
            maxId.set(getCurrentMaxIdRemote())
            var count = 0
            getAllExistingEntries().forEach {
                cache.put(it.key, it.value)
                ++count
            }
            initialized = true
            logger.info { "Initialized ${count} entries from MongoDB, current max id = ${maxId.get()}" }
        }
    }
}