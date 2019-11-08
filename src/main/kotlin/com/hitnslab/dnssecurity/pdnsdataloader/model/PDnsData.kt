package com.hitnslab.dnssecurity.pdnsdataloader.model

import com.google.common.net.InternetDomainName
import java.util.*


data class PDnsData(
        val queryTime: Date,
        val domain: String,
        val queryType: String,
        val replyCode: String,
        val topPrivateDomain: String
) {
    var clientIp: String? = null
    val ips = mutableSetOf<String>()
    lateinit var cnames: MutableSet<String>
        private set

    constructor(queryTime: Date, domain: String, queryType: String, replyCode: String) : this(
            queryTime,
            domain,
            queryType,
            replyCode,
            InternetDomainName.from(domain).topPrivateDomain().toString()
    ) {
        cnames = mutableSetOf()
    }

    constructor(data: PDnsDataDAO) : this(
            data.queryTime,
            data.domain,
            data.queryType,
            data.replyCode,
            data.topPrivateDomain
    ) {
        ips.addAll(data.ips.mapNotNull(PDnsDataDAO.Companion::intToIp))
        cnames = data.cnames
    }
}