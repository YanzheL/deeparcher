package com.hitnslab.dnssecurity.deeparcher.util

import com.google.common.net.InternetDomainName
import mu.KotlinLogging
import org.springframework.core.io.Resource
import java.util.function.Predicate

/**
 * @author Yanzhe Lee [lee.yanzhe@yanzhe.org]
 */
class DomainWhitelistPredicate : Predicate<String> {

    private val logger = KotlinLogging.logger {}

    /**
     * contains top private domains or public suffixes constructed from whitelist resource file
     */
    private val whitelist = mutableSetOf<String>()

    private val patterns = mutableListOf<Regex>()

    override fun test(t: String): Boolean {
        try {
            val dn = InternetDomainName.from(t)
            var key = dn.toString()
            for (pattern in patterns) {
                if (pattern.containsMatchIn(key)) {
                    return true
                }
            }
            when {
                dn.isUnderPublicSuffix -> key = dn.topPrivateDomain().toString()
                !dn.hasPublicSuffix() -> return false
            }
            return key in whitelist
        } catch (e: IllegalArgumentException) {
            logger.warn { "Cannot determine topic private domain of <$t>, exception <$e>" }
            return false
        }
    }

    fun fromResource(resource: Resource): DomainWhitelistPredicate {
        resource.inputStream
            .bufferedReader()
            .use { rd ->
                rd.lineSequence().forEach {
                    try {
                        fromFQDN(it)
                    } catch (e: IllegalArgumentException) {
                        logger.error { "Invalid whitelist item <$it>, exception <$e>" }
                    }
                }
            }
        return this
    }

    fun fromRegex(pattern: String): DomainWhitelistPredicate {
        patterns.add(Regex(pattern))
        return this
    }

    fun fromFQDN(fqdn: String): DomainWhitelistPredicate {
        val dn = InternetDomainName.from(fqdn)
        var key = dn.toString()
        when {
            dn.isUnderPublicSuffix -> key = dn.topPrivateDomain().toString()
            !dn.hasPublicSuffix() -> {
                logger.warn { "Whitelist item <$dn> does not have public suffix" }
                return this
            }
        }
        whitelist.add(key)
        return this
    }
}