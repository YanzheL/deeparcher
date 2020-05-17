package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainAssocDetailProto.DomainAssocDetail
import org.apache.kafka.common.serialization.Serializer

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class DomainAssocDetailProtoSerializer : Serializer<DomainAssocDetail> {

    override fun serialize(topic: String, data: DomainAssocDetail): ByteArray = data.toByteArray()
}