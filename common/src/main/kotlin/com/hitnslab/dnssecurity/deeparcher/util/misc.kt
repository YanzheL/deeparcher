package com.hitnslab.dnssecurity.deeparcher.util

import com.amazon.corretto.crypto.provider.AmazonCorrettoCryptoProvider
import com.amazon.corretto.crypto.provider.SelfTestStatus
import mu.KotlinLogging
import javax.crypto.Cipher

fun checkCorrettoCryptoProvider(): Boolean {
    val logger = KotlinLogging.logger {}
    val provider = Cipher.getInstance("AES/GCM/NoPadding").provider.name
    logger.info { "Current security provider in use: <$provider>" }
    if (provider == AmazonCorrettoCryptoProvider.PROVIDER_NAME) {
        logger.info { "<$provider> is installed." }
        if (AmazonCorrettoCryptoProvider.INSTANCE.loadingError == null && AmazonCorrettoCryptoProvider.INSTANCE.runSelfTests() == SelfTestStatus.PASSED) {
            logger.info { "<$provider> is healthy." }
            return true
        }
    }
    return false
}