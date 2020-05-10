package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.PDnsDataProto.PDnsData
import org.apache.kafka.common.serialization.Deserializer

class PDnsProtoDeserializer : Deserializer<PDnsData> {
    override fun deserialize(topic: String, data: ByteArray): PDnsData = PDnsData.parseFrom(data)
}