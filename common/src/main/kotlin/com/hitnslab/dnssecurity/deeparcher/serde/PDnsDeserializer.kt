package com.hitnslab.dnssecurity.deeparcher.serde

import com.hitnslab.dnssecurity.deeparcher.api.proto.PDnsDataProto
import com.hitnslab.dnssecurity.deeparcher.model.DnsQueryType
import com.hitnslab.dnssecurity.deeparcher.model.DnsRCode
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import org.apache.kafka.common.serialization.Deserializer
import java.net.InetAddress
import java.nio.ByteBuffer
import java.time.Instant

class PDnsDeserializer : Deserializer<PDnsData> {
    override fun deserialize(topic: String?, data: ByteArray?): PDnsData {
        val parsed = PDnsDataProto.PDnsData.parseFrom(data)
        val a = parsed.rCnamesList
        val pdnsData = PDnsData(
                queryTime = Instant.ofEpochMilli(parsed.qTime),
                domain = parsed.fqdn,
                queryType = DnsQueryType.fromInt(parsed.qType).name,
                replyCode = DnsRCode.fromInt(parsed.rCode).name
        )
        pdnsData.clientIp = InetAddress.getByAddress(parsed.clientIp.toByteArray()).hostAddress
        parseIpFromBytes(parsed.rIpv4Addrs.toByteArray(), 4, pdnsData.ips)
        parseIpFromBytes(parsed.rIpv6Addrs.toByteArray(), 16, pdnsData.ips)
        pdnsData.cnames.addAll(parsed.rCnamesList)
        return pdnsData
    }

    private fun parseIpFromBytes(bytesIn: ByteArray, width: Int, ipOut: MutableCollection<String>) {
        if (bytesIn.isEmpty()) {
            return
        }
        if (bytesIn.size % width != 0) {
            throw Exception("IP Address Bytes <$bytesIn> has incorrect length <${bytesIn.size}>.")
        }
        val buffer = ByteBuffer.allocate(width)
        for ((i, byte) in bytesIn.withIndex()) {
            buffer.put(byte)
            if (i != 0 && (i + 1) % width == 0) {
                ipOut.add(InetAddress.getByAddress(buffer.array()).hostAddress)
                buffer.clear()
            }
        }
    }
}