package com.hitnslab.dnssecurity.deeparcher.converter

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.DomainAssocDetailProto
import com.hitnslab.dnssecurity.deeparcher.model.DomainAssocDetail
import com.hitnslab.dnssecurity.deeparcher.util.parseIpFromBytes
import io.netty.buffer.Unpooled
import org.springframework.core.convert.converter.Converter

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class ProtoToDomainAssocDetailConverter :
    Converter<DomainAssocDetailProto.DomainAssocDetail, DomainAssocDetail> {
    override fun convert(source: DomainAssocDetailProto.DomainAssocDetail): DomainAssocDetail {
        val ret = DomainAssocDetail(
            source.fqdn,
            source.domain
        )
        parseIpFromBytes(Unpooled.wrappedBuffer(source.ipv4Addrs.asReadOnlyByteBuffer()), 4, ret.ipv4Addresses)
        parseIpFromBytes(Unpooled.wrappedBuffer(source.ipv6Addrs.asReadOnlyByteBuffer()), 16, ret.ipv6Addresses)
        ret.cnames.addAll(source.cnamesList)
        return ret
    }
}