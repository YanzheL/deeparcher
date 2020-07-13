package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.GraphEventProto.GraphEvent
import org.apache.kafka.common.serialization.Serializer

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class GraphEventProtoSerializer : Serializer<GraphEvent> {

    override fun serialize(topic: String, data: GraphEvent): ByteArray = data.toByteArray()
}