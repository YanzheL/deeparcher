package com.hitnslab.dnssecurity.deeparcher.model

import com.google.common.net.InternetDomainName
import java.time.Instant


data class PDnsData(
        val queryTime: Instant,
        val domain: String,
        val queryType: String,
        val replyCode: String
) {

    var clientIp: String? = null

    var topPrivateDomain: String? = null
        private set

    val ips = mutableSetOf<String>()

    val cnames = mutableSetOf<String>()

    init {
        topPrivateDomain = try {
            InternetDomainName.from(domain).topPrivateDomain().toString()
        } catch (e: Exception) {
            null
        }
    }
}