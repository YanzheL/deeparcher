package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.PDnsDataProto.PDnsData
import org.apache.kafka.common.serialization.Serializer

class PDnsProtoSerializer : Serializer<PDnsData> {

    override fun serialize(topic: String, data: PDnsData): ByteArray = PDnsData
            .newBuilder()
            .mergeFrom(data)
            .build()
            .toByteArray()
}