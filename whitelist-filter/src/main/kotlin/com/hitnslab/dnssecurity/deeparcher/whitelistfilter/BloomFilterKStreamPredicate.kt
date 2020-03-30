package com.hitnslab.dnssecurity.deeparcher.whitelistfilter

import com.google.common.hash.BloomFilter
import com.google.common.hash.Funnel
import org.apache.kafka.streams.kstream.Predicate as KPredicate


class BloomFilterKStreamPredicate<K, V>(funnel: Funnel<K>, expectedInsertions: Long, fpp: Double) : KPredicate<K, V> {

    val filter = BloomFilter.create(funnel, expectedInsertions, fpp)

    override fun test(key: K, value: V): Boolean {
        val seen = filter.mightContain(key)
        filter.put(key)
        return seen
    }
}