package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.GraphEventProto.GraphEvent
import org.apache.kafka.common.serialization.Deserializer

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class GraphEventProtoDeserializer : Deserializer<GraphEvent> {
    override fun deserialize(topic: String, data: ByteArray): GraphEvent =
        GraphEvent.parseFrom(data)
}