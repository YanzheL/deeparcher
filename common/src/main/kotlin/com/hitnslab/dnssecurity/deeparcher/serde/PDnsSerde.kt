package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer

class PDnsSerde : Serde<PDnsData> {
    override fun serializer(): Serializer<PDnsData> {
        return PDnsSerializer()
    }

    override fun deserializer(): Deserializer<PDnsData> {
        return PDnsDeserializer()
    }
}