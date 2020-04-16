package com.hitnslab.dnssecurity.deeparcher.converter

import com.google.protobuf.ByteString
import com.hitnslab.dnssecurity.deeparcher.api.proto.DomainIPAssocDetailProto
import com.hitnslab.dnssecurity.deeparcher.model.DomainIPAssocDetail
import io.netty.buffer.PooledByteBufAllocator
import org.springframework.core.convert.converter.Converter

class DomainIPAssocDetailToProtoConverter : Converter<DomainIPAssocDetail, DomainIPAssocDetailProto.DomainIPAssocDetail> {
    private val allocator = PooledByteBufAllocator.DEFAULT

    override fun convert(source: DomainIPAssocDetail): DomainIPAssocDetailProto.DomainIPAssocDetail {
        val builder = DomainIPAssocDetailProto.DomainIPAssocDetail
                .newBuilder()
                .setDomain(source.topPrivateDomain)
                .setFqdn(source.domain)
        val allIpv4Bytes = allocator.directBuffer(source.ipv4Addresses.size)
        val allIpv6Bytes = allocator.directBuffer(source.ipv6Addresses.size)
        source.ipv4Addresses.forEach {
            allIpv4Bytes.writeBytes(it.address)
        }
        source.ipv6Addresses.forEach {
            allIpv6Bytes.writeBytes(it.address)
        }
        builder.ipv4Addrs = ByteString.copyFrom(allIpv4Bytes.nioBuffer())
        builder.ipv6Addrs = ByteString.copyFrom(allIpv6Bytes.nioBuffer())
        allIpv4Bytes.release()
        allIpv6Bytes.release()
        return builder.build()
    }
}