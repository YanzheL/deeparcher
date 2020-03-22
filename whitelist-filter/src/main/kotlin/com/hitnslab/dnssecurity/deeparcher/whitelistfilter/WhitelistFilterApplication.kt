package com.hitnslab.dnssecurity.deeparcher.whitelistfilter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication(proxyBeanMethods = false)
@EnableConfigurationProperties
class WhitelistFilterApplication

fun main(args: Array<String>) {
    runApplication<WhitelistFilterApplication>(*args)
}
