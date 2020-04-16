package com.hitnslab.dnssecurity.deeparcher.converter

import com.hitnslab.dnssecurity.deeparcher.api.proto.DomainIPAssocDetailProto
import com.hitnslab.dnssecurity.deeparcher.model.DomainIPAssocDetail
import com.hitnslab.dnssecurity.deeparcher.util.parseIpFromBytes
import org.springframework.core.convert.converter.Converter

class ProtoToDomainIPAssocDetailConverter : Converter<DomainIPAssocDetailProto.DomainIPAssocDetail, DomainIPAssocDetail> {
    override fun convert(source: DomainIPAssocDetailProto.DomainIPAssocDetail): DomainIPAssocDetail {
        val ret = DomainIPAssocDetail(
                source.fqdn,
                source.domain
        )
        parseIpFromBytes(source.ipv4Addrs.asReadOnlyByteBuffer(), 4, ret.ipv4Addresses)
        parseIpFromBytes(source.ipv6Addrs.asReadOnlyByteBuffer(), 16, ret.ipv6Addresses)
        return ret
    }
}