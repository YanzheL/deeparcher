package com.hitnslab.dnssecurity.pdnsdataloader.model

import java.net.InetAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

data class PDNSInfo(
        val queryTime: Date,
        val domain: String,
        val queryType: String,
        val replyCode: String
) {
    var clientIp: Int = 0

    var clientIpString: String?
        get() = if (clientIp == 0) null else intToIp(clientIp)
        set(value) {
            clientIp = if (value == null) 0 else ipToInt(value)
        }

    val ips = mutableSetOf<Int>()

    val ipStrings: List<String>
        get() = ips.map(::intToIp)

    val cnames = mutableSetOf<String>()

    fun addIpStrings(vararg ip: String) {
        ips.addAll(ip.map(::ipToInt).filter { o -> o != 0 })
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

        fun intToIp(ip: Int): String {
            val bytes = ByteBuffer
                    .allocate(4)
                    .order(ByteOrder.BIG_ENDIAN)
                    .putInt(ip)
                    .array()
            return InetAddress.getByAddress(bytes).hostAddress
        }
    }
}