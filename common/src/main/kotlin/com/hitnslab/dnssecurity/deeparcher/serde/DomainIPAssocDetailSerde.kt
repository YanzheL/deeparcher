package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.model.DomainIPAssocDetail
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serde
import org.apache.kafka.common.serialization.Serializer

class DomainIPAssocDetailSerde : Serde<DomainIPAssocDetail> {
    override fun serializer(): Serializer<DomainIPAssocDetail> {
        return DomainIPAssocDetailSerializer()
    }

    override fun deserializer(): Deserializer<DomainIPAssocDetail> {
        return DomainIPAssocDetailDeserializer()
    }
}