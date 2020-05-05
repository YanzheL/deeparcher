package com.hitnslab.dnssecurity.deeparcher.converter

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainAssocDetailProto
import com.hitnslab.dnssecurity.deeparcher.model.DomainAssocDetail
import com.hitnslab.dnssecurity.deeparcher.util.parseIpFromBytes
import org.springframework.core.convert.converter.Converter

class ProtoToDomainAssocDetailConverter :
    Converter<DomainAssocDetailProto.DomainAssocDetail, DomainAssocDetail> {
    override fun convert(source: DomainAssocDetailProto.DomainAssocDetail): DomainAssocDetail {
        val ret = DomainAssocDetail(
            source.fqdn,
            source.domain
        )
        parseIpFromBytes(source.ipv4Addrs.asReadOnlyByteBuffer(), 4, ret.ipv4Addresses)
        parseIpFromBytes(source.ipv6Addrs.asReadOnlyByteBuffer(), 16, ret.ipv6Addresses)
        ret.cnames.addAll(source.cnamesList)
        return ret
    }
}