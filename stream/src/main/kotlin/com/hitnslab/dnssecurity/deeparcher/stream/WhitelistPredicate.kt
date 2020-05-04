package com.hitnslab.dnssecurity.deeparcher.stream

import com.google.common.net.InternetDomainName
import mu.KotlinLogging
import org.springframework.core.io.Resource
import java.util.function.Predicate

class WhitelistPredicate : Predicate<String> {

    private val logger = KotlinLogging.logger {}

    private val topPrivateDomainWhitelist = mutableSetOf<String>()

    private val patterns = mutableListOf<Regex>()

    override fun test(t: String): Boolean {
        for (pattern in patterns) {
            if (pattern.containsMatchIn(t)) {
                return true
            }
        }
        return try {
            val tpd = InternetDomainName.from(t).topPrivateDomain().toString()
            tpd in topPrivateDomainWhitelist
        } catch (e: RuntimeException) {
            logger.warn { "Cannot determine topic private domain of <$t>, exception <$e>" }
            false
        }
    }

    fun fromResource(resource: Resource): WhitelistPredicate {
        resource.inputStream
            .bufferedReader()
            .use { rd ->
                rd.lineSequence().forEach {
                    try {
                        fromFQDN(it)
                    } catch (e: Exception) {
                        logger.error { "Invalid whitelist item <$it>" }
                    }
                }
            }
        return this
    }

    fun fromRegex(pattern: String): WhitelistPredicate {
        patterns.add(Regex(pattern))
        return this
    }

    fun fromFQDN(fqdn: String): WhitelistPredicate {
        val dn = InternetDomainName.from(fqdn)
        topPrivateDomainWhitelist.add(dn.topPrivateDomain().toString())
        return this
    }
}