package com.hitnslab.dnssecurity.deeparcher.stream

import com.google.common.net.InternetDomainName
import mu.KotlinLogging
import org.springframework.core.io.Resource
import java.util.function.Predicate

class WhitelistPredicate : Predicate<String> {

    private val logger = KotlinLogging.logger {}

    private val whitelist = mutableSetOf<String>()

    private val patterns = mutableListOf<Regex>()

    override fun test(t: String): Boolean {
        for (pattern in patterns) {
            if (pattern.containsMatchIn(t)) {
                return true
            }
        }
        return t in whitelist
    }

    fun fromResource(resource: Resource): WhitelistPredicate {
        val reader = resource.inputStream.bufferedReader()
        reader.use {
            reader.lines().forEach {
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
        whitelist.add(InternetDomainName.from(fqdn).topPrivateDomain().toString())
        return this
    }
}