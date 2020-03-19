package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader.parsing

import com.google.common.net.InetAddresses
import com.google.common.net.InternetDomainName
import com.hitnslab.dnssecurity.deeparcher.model.DnsQueryType
import com.hitnslab.dnssecurity.deeparcher.model.DnsRCode
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import mu.KotlinLogging
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class PDnsDataValidator : ItemProcessor<PDnsData, PDnsData?> {

    private val logger = KotlinLogging.logger {}

    override fun process(item: PDnsData): PDnsData? {
        if (item.topPrivateDomain == null)
            return null
        if (item.clientIp != null && !InetAddresses.isInetAddress(item.clientIp!!))
            return null
        for (ip in item.ips) {
            if (!InetAddresses.isInetAddress(ip))
                return null
        }
        try {
            for (cname in item.cnames) {
                if (!InternetDomainName.from(cname).isUnderPublicSuffix)
                    return null
            }
            DnsQueryType.valueOf(item.queryType)
            DnsRCode.valueOf(item.replyCode)
        } catch (e: Exception) {
            return null
        }
        return item
    }
}