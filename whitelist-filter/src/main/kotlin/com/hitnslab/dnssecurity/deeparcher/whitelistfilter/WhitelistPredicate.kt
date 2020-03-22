package com.hitnslab.dnssecurity.deeparcher.whitelistfilter

import com.google.common.net.InternetDomainName
import com.hitnslab.dnssecurity.deeparcher.model.PDnsData
import mu.KotlinLogging
import org.springframework.core.io.FileSystemResource
import java.nio.file.Path
import java.util.function.Predicate

class WhitelistPredicate(whitelistPath: String) : Predicate<PDnsData> {

    private val logger = KotlinLogging.logger {}

    val whitelist = mutableSetOf<String>()

    init {
        val reader = FileSystemResource(Path.of(whitelistPath)).inputStream.bufferedReader()
        reader.use {
            reader.lines().forEach {
                try {
                    whitelist.add(InternetDomainName.from(it).topPrivateDomain().toString())
                } catch (e: Exception) {
                    logger.error { "Invalid whitelist item <$it>" }
                }
            }
        }
    }

    override fun test(t: PDnsData): Boolean {
        return t.topPrivateDomain in whitelist
    }
}