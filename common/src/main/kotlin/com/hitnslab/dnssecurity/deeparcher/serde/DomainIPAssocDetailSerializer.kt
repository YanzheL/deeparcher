package com.hitnslab.dnssecurity.deeparcher.serde

import com.google.protobuf.ByteString
import com.hitnslab.dnssecurity.deeparcher.api.proto.DomainIPAssocDetailProto
import com.hitnslab.dnssecurity.deeparcher.model.DomainIPAssocDetail
import io.netty.buffer.PooledByteBufAllocator
import org.apache.kafka.common.serialization.Serializer

class DomainIPAssocDetailSerializer : Serializer<DomainIPAssocDetail> {
    private val allocator = PooledByteBufAllocator.DEFAULT

    override fun serialize(topic: String, data: DomainIPAssocDetail): ByteArray {
        val builder = DomainIPAssocDetailProto.DomainIPAssocDetail
                .newBuilder()
                .setDomain(data.topPrivateDomain)
                .setFqdn(data.domain)
        val allIpv4Bytes = allocator.directBuffer(data.ipv4Addresses.size)
        val allIpv6Bytes = allocator.directBuffer(data.ipv6Addresses.size)
        data.ipv4Addresses.forEach {
            allIpv4Bytes.writeBytes(it.address)
        }
        data.ipv6Addresses.forEach {
            allIpv6Bytes.writeBytes(it.address)
        }
        builder.ipv4Addrs = ByteString.copyFrom(allIpv4Bytes.nioBuffer())
        builder.ipv6Addrs = ByteString.copyFrom(allIpv6Bytes.nioBuffer())
        allIpv4Bytes.release()
        allIpv6Bytes.release()
        return builder.build().toByteArray()
    }
}