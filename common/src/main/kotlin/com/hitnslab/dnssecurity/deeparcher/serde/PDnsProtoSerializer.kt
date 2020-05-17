package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.PDnsDataProto.PDnsData
import org.apache.kafka.common.serialization.Serializer

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class PDnsProtoSerializer : Serializer<PDnsData> {

    override fun serialize(topic: String, data: PDnsData): ByteArray = data.toByteArray()
}