package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate
import org.apache.kafka.common.serialization.Deserializer

class GraphAssocEdgeUpdateProtoDeserializer : Deserializer<GraphAssocEdgeUpdate> {
    override fun deserialize(topic: String, data: ByteArray): GraphAssocEdgeUpdate =
        GraphAssocEdgeUpdate.parseFrom(data)
}