package com.hitnslab.dnssecurity.deeparcher.stream

import java.util.function.Predicate
import org.apache.kafka.streams.kstream.Predicate as KPredicate

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class KStreamValuePredicateAdapter<K, V>(val predicate: Predicate<V>) : KPredicate<K, V> {
    override fun test(key: K, value: V) = predicate.test(value)
}