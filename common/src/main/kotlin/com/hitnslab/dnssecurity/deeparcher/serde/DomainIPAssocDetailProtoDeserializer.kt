package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainIPAssocDetailProto.DomainIPAssocDetail
import org.apache.kafka.common.serialization.Deserializer

class DomainIPAssocDetailProtoDeserializer : Deserializer<DomainIPAssocDetail> {
    override fun deserialize(topic: String, data: ByteArray): DomainIPAssocDetail = DomainIPAssocDetail.parseFrom(data)
}