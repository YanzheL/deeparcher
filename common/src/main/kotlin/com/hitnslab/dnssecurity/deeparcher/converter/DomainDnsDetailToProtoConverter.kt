package com.hitnslab.dnssecurity.deeparcher.converter

import com.google.protobuf.ByteString
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.DomainDnsDetailProto
import com.hitnslab.dnssecurity.deeparcher.model.DomainDnsDetail
import io.netty.buffer.PooledByteBufAllocator
import org.springframework.core.convert.converter.Converter

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class DomainDnsDetailToProtoConverter : Converter<DomainDnsDetail, DomainDnsDetailProto.DomainDnsDetail> {
    private val allocator = PooledByteBufAllocator.DEFAULT

    override fun convert(source: DomainDnsDetail): DomainDnsDetailProto.DomainDnsDetail {
        val builder = DomainDnsDetailProto.DomainDnsDetail
            .newBuilder()
            .setDomain(source.topPrivateDomain)
            .setFqdn(source.domain)
        val allIpv4Bytes = allocator.heapBuffer(source.ipv4Addresses.size)
        val allIpv6Bytes = allocator.heapBuffer(source.ipv6Addresses.size)
        source.ipv4Addresses.forEach {
            allIpv4Bytes.writeBytes(it.address)
        }
        source.ipv6Addresses.forEach {
            allIpv6Bytes.writeBytes(it.address)
        }
        builder.ipv4Addrs = ByteString.copyFrom(allIpv4Bytes.nioBuffer())
        builder.ipv6Addrs = ByteString.copyFrom(allIpv6Bytes.nioBuffer())
        builder.addAllCnames(source.cnames)
        allIpv4Bytes.release()
        allIpv6Bytes.release()
        return builder.build()
    }
}