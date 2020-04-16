package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.DomainIPAssocDetailProto
import com.hitnslab.dnssecurity.deeparcher.model.DomainIPAssocDetail
import com.hitnslab.dnssecurity.deeparcher.util.parseIpFromBytes
import org.apache.kafka.common.serialization.Deserializer

class DomainIPAssocDetailDeserializer : Deserializer<DomainIPAssocDetail> {
    override fun deserialize(topic: String, data: ByteArray): DomainIPAssocDetail {
        val parsed = DomainIPAssocDetailProto.DomainIPAssocDetail.parseFrom(data)
        val ret = DomainIPAssocDetail(
                parsed.fqdn,
                parsed.domain
        )
        parseIpFromBytes(parsed.ipv4Addrs.asReadOnlyByteBuffer(), 4, ret.ipv4Addresses)
        parseIpFromBytes(parsed.ipv6Addrs.asReadOnlyByteBuffer(), 16, ret.ipv6Addresses)
        return ret
    }
}