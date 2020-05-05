package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.GraphAssocEdgeUpdateProto.GraphAssocEdgeUpdate
import org.apache.kafka.common.serialization.Serializer

class GraphAssocEdgeUpdateProtoSerializer : Serializer<GraphAssocEdgeUpdate> {

    override fun serialize(topic: String, data: GraphAssocEdgeUpdate): ByteArray = data.toByteArray()
}