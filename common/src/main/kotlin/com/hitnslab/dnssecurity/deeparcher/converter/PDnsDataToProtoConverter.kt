package com.hitnslab.dnssecurity.deeparcher.converter

import com.google.protobuf.ByteString
import com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsDataProto
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import io.netty.buffer.PooledByteBufAllocator
import org.springframework.core.convert.converter.Converter
import java.net.Inet4Address
import java.net.Inet6Address


class PDnsDataToProtoConverter : Converter<PDnsData, PDnsDataProto.PDnsData> {

    private val allocator = PooledByteBufAllocator.DEFAULT

    override fun convert(source: PDnsData): PDnsDataProto.PDnsData {
        val builder = PDnsDataProto.PDnsData
                .newBuilder()
                .setQTime(source!!.queryTime.toEpochMilli())
                .setDomain(source.topPrivateDomain)
                .setQType(source.queryType.value)
                .setRCode(source.replyCode.value)
                .setFqdn(source.domain)
                .setClientIp(ByteString.copyFrom(source.clientIp?.address))
        if (source.ips != null) {
            val allIpv4Bytes = allocator.directBuffer(source.ips.size)
            val allIpv6Bytes = allocator.directBuffer(source.ips.size)
            source.ips.forEach {
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
        builder.addAllRCnames(source.cnames)
        return builder.build()
    }
}