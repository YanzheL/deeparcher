package com.hitnslab.dnssecurity.pdnsdataloader.model

import com.google.common.net.InternetDomainName
import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.time.Instant


data class PDnsDataDAO(
        val queryTime: Instant,
        val domain: String,
        val queryType: DnsQueryType,
        val replyCode: DnsRCode,
        val topPrivateDomain: String
) {
    var clientIp: Int = 0
    val ips = mutableSetOf<Int>()
    lateinit var cnames: MutableSet<String>
        private set

    constructor(queryTime: Instant, domain: String, queryType: String, replyCode: String) : this(
            queryTime,
            domain,
            DnsQueryType.valueOf(queryType),
            DnsRCode.valueOf(replyCode),
            InternetDomainName.from(domain).topPrivateDomain().toString()
    ) {
        cnames = mutableSetOf()
    }

    constructor(data: PDnsData) : this(
            data.queryTime,
            data.domain,
            DnsQueryType.valueOf(data.queryType),
            DnsRCode.valueOf(data.replyCode),
            data.topPrivateDomain
    ) {
        clientIp = ipToInt(data.clientIp)
        ips.addAll(data.ips.map(::ipToInt).filter { o -> o != 0 })
        cnames = data.cnames
    }

    companion object {
        fun ipToInt(ip: String?): Int {
            if (ip == null)
                return 0
            if (ip.isEmpty())
                return 0
            val bytes = InetAddress.getByName(ip).address
            if (bytes.size != 4)
                return 0
            return ByteBuffer.allocate(Integer.BYTES)
                    .order(ByteOrder.BIG_ENDIAN)
                    .put(bytes)
                    .getInt(0)
        }

        fun intToIp(ip: Int): String? {
            if (ip == 0)
                return null
            val bytes = ByteBuffer
                    .allocate(4)
                    .order(ByteOrder.BIG_ENDIAN)
                    .putInt(ip)
                    .array()
            return InetAddress.getByAddress(bytes).hostAddress
        }
    }

}