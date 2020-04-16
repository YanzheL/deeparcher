package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.DomainIPAssocDetailProto.DomainIPAssocDetail
import org.apache.kafka.common.serialization.Serializer

class DomainIPAssocDetailProtoSerializer : Serializer<DomainIPAssocDetail> {

    override fun serialize(topic: String, data: DomainIPAssocDetail): ByteArray = DomainIPAssocDetail
            .newBuilder()
            .mergeFrom(data)
            .build()
            .toByteArray()
}