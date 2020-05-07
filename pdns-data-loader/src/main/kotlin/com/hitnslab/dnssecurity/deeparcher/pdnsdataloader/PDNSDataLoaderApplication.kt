package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader

import com.amazon.corretto.crypto.provider.AmazonCorrettoCryptoProvider
import com.hitnslab.dnssecurity.deeparcher.util.checkCorrettoCryptoProvider
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(proxyBeanMethods = false)
class PDNSDataLoaderApplication

fun main(args: Array<String>) {
    AmazonCorrettoCryptoProvider.install()
    checkCorrettoCryptoProvider()
    runApplication<PDNSDataLoaderApplication>(*args)
}
