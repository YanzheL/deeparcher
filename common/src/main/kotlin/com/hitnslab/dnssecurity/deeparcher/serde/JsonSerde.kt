package com.hitnslab.dnssecurity.deeparcher.serde

import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer
import kotlin.reflect.KClass

class JsonSerde<T : Any>(val clz: KClass<T>) : Serde<T> {

    override fun serializer(): Serializer<T> {
        return JsonSerializer()
    }

    override fun deserializer(): Deserializer<T> {
        return JsonDeserializer(clz)
    }
}