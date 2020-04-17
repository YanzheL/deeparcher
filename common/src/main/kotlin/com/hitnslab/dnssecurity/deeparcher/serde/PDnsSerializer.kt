package com.hitnslab.dnssecurity.deeparcher.serde

import com.google.protobuf.ByteString
import com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsDataProto
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import io.netty.buffer.PooledByteBufAllocator
import org.apache.kafka.common.serialization.Serializer
import java.net.Inet4Address
import java.net.Inet6Address

class PDnsSerializer : Serializer<PDnsData> {

    private val allocator = PooledByteBufAllocator.DEFAULT

    override fun serialize(topic: String, data: PDnsData): ByteArray {
        val builder = PDnsDataProto.PDnsData
                .newBuilder()
                .setQTime(data!!.queryTime.toEpochMilli())
                .setDomain(data.topPrivateDomain)
                .setQType(data.queryType.value)
                .setRCode(data.replyCode.value)
                .setFqdn(data.domain)
                .setClientIp(ByteString.copyFrom(data.clientIp?.address))
        if (data.ips != null) {
            val allIpv4Bytes = allocator.heapBuffer(data.ips.size)
            val allIpv6Bytes = allocator.heapBuffer(data.ips.size)
            data.ips.forEach {
                when (it) {
                    is Inet4Address -> allIpv4Bytes.writeBytes(it.address)
                    is Inet6Address -> allIpv6Bytes.writeBytes(it.address)
                }
            }
            builder.rIpv4Addrs = ByteString.copyFrom(allIpv4Bytes.nioBuffer())
            builder.rIpv6Addrs = ByteString.copyFrom(allIpv6Bytes.nioBuffer())
            allIpv4Bytes.release()
            allIpv6Bytes.release()
        }
        builder.addAllRCnames(data.cnames)
        return builder.build().toByteArray()
    }
}