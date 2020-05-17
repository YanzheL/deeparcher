package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.DomainDnsDetailProto.DomainDnsDetail
import org.apache.kafka.common.serialization.Serializer

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class DomainDnsDetailProtoSerializer : Serializer<DomainDnsDetail> {

    override fun serialize(topic: String, data: DomainDnsDetail): ByteArray = data.toByteArray()
}