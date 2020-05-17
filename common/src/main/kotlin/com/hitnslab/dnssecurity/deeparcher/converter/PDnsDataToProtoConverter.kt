package com.hitnslab.dnssecurity.deeparcher.converter

import com.google.protobuf.ByteString
import com.hitnslab.dnssecurity.deeparcher.api.proto.generated.java.PDnsDataProto
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import io.netty.buffer.PooledByteBufAllocator
import org.springframework.core.convert.converter.Converter
import java.net.Inet4Address
import java.net.Inet6Address

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class PDnsDataToProtoConverter : Converter<PDnsData, PDnsDataProto.PDnsData> {

    private val allocator = PooledByteBufAllocator.DEFAULT

    override fun convert(source: PDnsData): PDnsDataProto.PDnsData {
        val builder = PDnsDataProto.PDnsData
            .newBuilder()
            .setQTime(source.queryTime.toEpochMilli())
            .setDomain(source.topPrivateDomain)
            .setQType(source.queryType.value)
            .setRCode(source.replyCode.value)
            .setFqdn(source.domain)
        source.clientIp?.let {
            builder.setClientIp(ByteString.copyFrom(it.address))
        }
        source.ips?.let { ips ->
            val allIpv4Bytes = allocator.heapBuffer(ips.size)
            val allIpv6Bytes = allocator.heapBuffer(ips.size)
            ips.forEach {
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
        source.cnames?.let { builder.addAllRCnames(it) }
        return builder.build()
    }
}