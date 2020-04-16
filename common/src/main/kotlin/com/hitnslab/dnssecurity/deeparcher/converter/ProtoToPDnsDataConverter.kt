package com.hitnslab.dnssecurity.deeparcher.converter

import com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsDataProto
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import com.hitnslab.dnssecurity.deeparcher.util.parseIpFromBytes
import org.springframework.core.convert.converter.Converter
import java.net.InetAddress

class ProtoToPDnsDataConverter : Converter<PDnsDataProto.PDnsData, PDnsData> {
    override fun convert(parsed: PDnsDataProto.PDnsData): PDnsData {
        val builder = PDnsData.Builder()
                .queryTime(parsed.qTime)
                .domain(parsed.fqdn)
                .queryType(parsed.qType)
                .replyCode(parsed.rCode)
                .topPrivateDomain(parsed.domain)
                .clientIp(parsed.clientIp.toByteArray())

        val ips = mutableSetOf<InetAddress>()
        parseIpFromBytes(parsed.rIpv4Addrs.asReadOnlyByteBuffer(), 4, ips)
        parseIpFromBytes(parsed.rIpv6Addrs.asReadOnlyByteBuffer(), 16, ips)
        builder.setIps(ips)
        val cnames = mutableSetOf<String>()
        cnames.addAll(parsed.rCnamesList)
        builder.setCNames(cnames)
        val data = builder.build()
        val err = builder.error
        if (data != null) {
            return data
        }
        if (err != null) {
            throw err
        }
        throw Exception("Unknown error")
    }
}