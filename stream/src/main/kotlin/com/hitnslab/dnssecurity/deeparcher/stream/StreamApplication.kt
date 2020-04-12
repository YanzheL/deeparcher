package com.hitnslab.dnssecurity.deeparcher.stream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(proxyBeanMethods = false)
class WhitelistFilterApplication

fun main(args: Array<String>) {
    runApplication<WhitelistFilterApplication>(*args)
}
