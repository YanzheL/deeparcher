package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsDataProto
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import com.hitnslab.dnssecurity.deeparcher.util.parseIpFromBytes
import org.apache.kafka.common.serialization.Deserializer
import java.net.InetAddress

class PDnsDeserializer : Deserializer<PDnsData> {
    override fun deserialize(topic: String, data: ByteArray): PDnsData {
        val parsed = PDnsDataProto.PDnsData.parseFrom(data)
        val builder = PDnsData.Builder()
                .queryTime(parsed.qTime)
                .domain(parsed.fqdn)
                .queryType(parsed.qType)
                .replyCode(parsed.rCode)
                .topPrivateDomain(parsed.domain)
                .clientIp(parsed.clientIp.toByteArray())

        val ips = mutableSetOf<InetAddress>()
        parseIpFromBytes(parsed.rIpv4Addrs.toByteArray(), 4, ips)
        parseIpFromBytes(parsed.rIpv6Addrs.toByteArray(), 16, ips)
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