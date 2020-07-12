package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.DomainDnsDetailProto.DomainDnsDetail
import org.apache.kafka.common.serialization.Deserializer

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class DomainDnsDetailProtoDeserializer : Deserializer<DomainDnsDetail> {
    override fun deserialize(topic: String, data: ByteArray): DomainDnsDetail = DomainDnsDetail.parseFrom(data)
}