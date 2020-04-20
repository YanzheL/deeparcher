package com.hitnslab.dnssecurity.deeparcher.model

import com.google.common.net.InetAddresses
import com.google.common.net.InternetDomainName
import com.hitnslab.dnssecurity.deeparcher.error.PDNSInvalidFieldException
import mu.KotlinLogging
import java.net.InetAddress
import java.net.URI
import java.net.URISyntaxException
import java.net.UnknownHostException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


data class PDnsData(
    val queryTime: Instant,
    val domain: String,
    val queryType: DnsQueryType,
    val replyCode: DnsRCode,
    val topPrivateDomain: String,
    val clientIp: InetAddress?,
    val ips: Set<InetAddress>?,
    val cnames: Set<String>?
) {

    class Builder {
        private var queryTime: Instant? = null
        private var domain: String? = null
        private var queryType: DnsQueryType? = null
        private var replyCode: DnsRCode? = null
        private var topPrivateDomain: String? = null
        private var clientIp: InetAddress? = null
        private var ips: MutableSet<InetAddress>? = null
        private var cnames: MutableSet<String>? = null
        private val logger = KotlinLogging.logger {}
        var error: Throwable? = null
            private set

        fun queryTime(value: String, fmt: DateTimeFormatter) = apply {
            queryTime = try {
                ZonedDateTime.of(
                    LocalDateTime.parse(value, fmt),
                    ZoneId.systemDefault()
                ).toInstant().truncatedTo(ChronoUnit.MILLIS)
            } catch (e: Exception) {
                logger.debug { "Invalid queryTime<$value>" }
                null
            }
        }

        fun queryTime(value: Long) = apply {
            queryTime = try {
                Instant.ofEpochMilli(value)
            } catch (e: Exception) {
                logger.debug { "Invalid queryTime<$value>" }
                null
            }
        }

        fun queryTime(value: Instant) = apply { queryTime = value }
        fun domain(value: String) = apply {
            domain = try {
                val uri = URI(value) // value maybe a URI
                val host = if (uri.scheme == null) uri.path else uri.host
                if (InternetDomainName.isValid(host)) {
                    host.toLowerCase()
                } else {
                    logger.debug { "Invalid domain<$host> fromm URI<$value>" }
                    null
                }
            } catch (e: URISyntaxException) {
                logger.debug { "Invalid URI<$value>" }
                null
            }

        }

        fun queryType(value: String) = apply {
            queryType = try {
                DnsQueryType.valueOf(value)
            } catch (e: Exception) {
                logger.debug { "Invalid queryType<$value>" }
                null
            }
        }

        fun queryType(value: Int) = apply {
            queryType = try {
                DnsQueryType.fromInt(value)
            } catch (e: Exception) {
                logger.debug { "Invalid queryType<$value>" }
                null
            }
        }

        fun replyCode(value: String) = apply {
            replyCode = try {
                DnsRCode.valueOf(value)
            } catch (e: Exception) {
                logger.debug { "Invalid replyCode<$value>" }
                null
            }
        }

        fun replyCode(value: Int) = apply {
            replyCode = try {
                DnsRCode.fromInt(value)
            } catch (e: Exception) {
                logger.debug { "Invalid replyCode<$value>" }
                null
            }
        }

        fun topPrivateDomain(value: String) = apply { topPrivateDomain = value.toLowerCase() }
        fun clientIp(value: String) = apply {
            clientIp = if (InetAddresses.isInetAddress(value)) {
                InetAddress.getByName(value)
            } else {
                logger.debug { "Invalid IP<$value>" }
                null
            }
        }

        fun clientIp(value: InetAddress) = apply { clientIp = value }
        fun clientIp(value: ByteArray) = apply {
            clientIp = if (value.isEmpty()) {
                logger.debug { "Invalid clientIp bytes<$value>" }
                null
            } else {
                try {
                    InetAddress.getByAddress(value)
                } catch (e: UnknownHostException) {
                    logger.debug { "Invalid clientIp bytes<$value>" }
                    null
                }
            }
        }

        fun setIps(value: MutableSet<InetAddress>) = apply { ips = value }
        fun addIp(value: String) = apply {
            if (InetAddresses.isInetAddress(value)) {
                addIp(InetAddress.getByName(value))
            } else {
                logger.debug { "Invalid IP<$value>" }
            }
        }

        fun addIp(value: InetAddress) = apply {
            if (ips == null) {
                ips = mutableSetOf()
            }
            ips!!.add(value)
        }

        fun addIp(value: ByteArray) = addIp(InetAddress.getByAddress(value))
        fun setCNames(value: MutableSet<String>) = apply { cnames = value }
        fun addCName(value: String) = apply {
            if (cnames == null) {
                cnames = mutableSetOf()
            }
            cnames!!.add(value)
        }

        fun build(): PDnsData? {
            error = null
            if (queryTime == null) {
                error = PDNSInvalidFieldException("queryTime cannot be null")
                return null
            }
            if (domain == null) {
                error = PDNSInvalidFieldException("domain cannot be null")
                return null
            }
            if (queryType == null) {
                error = PDNSInvalidFieldException("queryType cannot be null")
                return null
            }
            if (replyCode == null) {
                error = PDNSInvalidFieldException("replyCode cannot be null")
                return null
            }
            if (topPrivateDomain == null) {
                try {
                    topPrivateDomain = InternetDomainName.from(domain).topPrivateDomain().toString()
                } catch (e: IllegalStateException) {
                    error = e
                    return null
                }
            }
            return PDnsData(
                queryTime!!,
                domain!!,
                queryType!!,
                replyCode!!,
                topPrivateDomain!!,
                clientIp,
                ips,
                cnames
            )
        }

    }
}