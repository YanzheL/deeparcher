package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainAssocDetailProto.DomainAssocDetail
import org.apache.kafka.common.serialization.Deserializer

class DomainAssocDetailProtoDeserializer : Deserializer<DomainAssocDetail> {
    override fun deserialize(topic: String, data: ByteArray) = DomainAssocDetail.parseFrom(data)
}