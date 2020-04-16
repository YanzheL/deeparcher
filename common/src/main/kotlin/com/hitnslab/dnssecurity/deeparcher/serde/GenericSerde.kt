package com.hitnslab.dnssecurity.deeparcher.serde

import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class GenericSerde
<T, ST : Serializer<T>, DT : Deserializer<T>>
(val serializer: KClass<ST>, val deserializer: KClass<DT>) : Serde<T> {

    override fun serializer(): Serializer<T> {
        return serializer.primaryConstructor!!.call()
    }

    override fun deserializer(): Deserializer<T> {
        return deserializer.primaryConstructor!!.call()
    }
}