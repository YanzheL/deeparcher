package com.hitnslab.dnssecurity.deeparcher.converter

import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.DomainDnsDetailProto
import com.hitnslab.dnssecurity.deeparcher.model.DomainDnsDetail
import com.hitnslab.dnssecurity.deeparcher.util.parseIpFromBytes
import io.netty.buffer.Unpooled
import org.springframework.core.convert.converter.Converter

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class ProtoToDomainDnsDetailConverter :
    Converter<DomainDnsDetailProto.DomainDnsDetail, DomainDnsDetail> {
    override fun convert(source: DomainDnsDetailProto.DomainDnsDetail): DomainDnsDetail {
        val ret = DomainDnsDetail(
            source.fqdn,
            source.domain
        )
        parseIpFromBytes(Unpooled.wrappedBuffer(source.ipv4Addrs.asReadOnlyByteBuffer()), 4, ret.ipv4Addresses)
        parseIpFromBytes(Unpooled.wrappedBuffer(source.ipv6Addrs.asReadOnlyByteBuffer()), 16, ret.ipv6Addresses)
        ret.cnames.addAll(source.cnamesList)
        return ret
    }
}