package com.hitnslab.dnssecurity.deeparcher.serde

import com.alibaba.fastjson.JSON
import org.apache.kafka.common.serialization.Serializer

class JsonSerializer<T> : Serializer<T> {

    override fun serialize(topic: String?, data: T): ByteArray {
        return JSON.toJSONBytes(data)
    }
}