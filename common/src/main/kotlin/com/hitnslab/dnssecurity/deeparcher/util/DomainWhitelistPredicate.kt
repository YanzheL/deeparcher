package com.hitnslab.dnssecurity.deeparcher.util

import com.google.common.net.InternetDomainName
import mu.KotlinLogging
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.AutoDetectParser
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.RecursiveParserWrapper
import org.apache.tika.sax.BasicContentHandlerFactory
import org.apache.tika.sax.RecursiveParserWrapperHandler
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
    val whitelist = mutableSetOf<String>()

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
        val handler = RecursiveParserWrapperHandler(
            BasicContentHandlerFactory(BasicContentHandlerFactory.HANDLER_TYPE.TEXT, -1)
        )
        val context = ParseContext()
        val parser = RecursiveParserWrapper(AutoDetectParser())
        context[RecursiveParserWrapper::class.java] = parser
        parser.parse(resource.inputStream, handler, Metadata(), context)
        var loaded = 0
        handler.metadataList
            .filter { it["Content-Type"].startsWith("text/plain") }
            .forEach { metadata ->
                metadata["X-TIKA:content"].lineSequence()
                    .filter { it.isNotEmpty() }
                    .forEach {
                        try {
                            fromFQDN(it)
                            ++loaded
                        } catch (e: IllegalArgumentException) {
                            logger.error { "Invalid whitelist item <$it>, exception <$e>" }
                        }
                    }
            }
        logger.info { "Loaded <$loaded> whitelist items from <$resource>" }
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