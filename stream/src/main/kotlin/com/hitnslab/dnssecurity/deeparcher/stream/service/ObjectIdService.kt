package com.hitnslab.dnssecurity.deeparcher.stream.service

interface ObjectIdService<K> {

    fun getCurrentMaxId(): Long

    fun getExistingId(key: K): Long?

    fun getExistingIds(keys: Iterable<K>): Sequence<Long?> {
        return keys.asSequence().map(::getExistingId)
    }

    fun getOrCreateId(key: K): Long

    fun getAllExistingEntries(): Sequence<Map.Entry<K, Long>>

    fun getOrCreateIds(keys: Iterable<K>): Sequence<Long> {
        return keys.asSequence().map(::getOrCreateId)
    }

    fun commit(): Boolean {
        return true
    }

    fun close() {}
}