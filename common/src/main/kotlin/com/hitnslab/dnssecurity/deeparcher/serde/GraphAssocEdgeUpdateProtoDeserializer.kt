package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate
import org.apache.kafka.common.serialization.Deserializer

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class GraphAssocEdgeUpdateProtoDeserializer : Deserializer<GraphAssocEdgeUpdate> {
    override fun deserialize(topic: String, data: ByteArray): GraphAssocEdgeUpdate =
        GraphAssocEdgeUpdate.parseFrom(data)
}