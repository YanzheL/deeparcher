package com.hitnslab.dnssecurity.deeparcher.serde

import com.google.protobuf.ByteString
import com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsDataProto
import com.hitnslab.dnssecurity.deeparcher.model.DnsQueryType
import com.hitnslab.dnssecurity.deeparcher.model.DnsRCode
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import org.apache.kafka.common.serialization.Serializer
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetAddress

class PDnsSerializer : Serializer<PDnsData> {
    override fun serialize(topic: String?, data: PDnsData?): ByteArray {
        val builder = PDnsDataProto.PDnsData
                .newBuilder()
                .setQTime(data!!.queryTime.epochSecond)
                .setDomain(data.topPrivateDomain)
                .setQType(DnsQueryType.valueOf(data.queryType).value)
                .setRCode(DnsRCode.valueOf(data.replyCode).value)
                .setFqdn(data.domain)
                .setClientIp(ByteString.copyFrom(InetAddress.getByName(data.clientIp).address))
        val allIpv4Bytes = mutableListOf<Byte>()
        val allIpv6Bytes = mutableListOf<Byte>()
        for (ip in data.ips) {
            val inetAddr = InetAddress.getByName(ip)
            val bytes = inetAddr.address.toList()
            when (inetAddr) {
                is Inet4Address -> allIpv4Bytes.addAll(bytes)
                is Inet6Address -> allIpv6Bytes.addAll(bytes)
            }
        }
        builder.rIpv4Addrs = ByteString.copyFrom(allIpv4Bytes.toByteArray())
        builder.rIpv6Addrs = ByteString.copyFrom(allIpv6Bytes.toByteArray())
        builder.addAllRCnames(data.cnames)
        return builder.build().toByteArray()
    }
}