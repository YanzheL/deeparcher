package com.hitnslab.dnssecurity.deeparcher.stream

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(proxyBeanMethods = false)
class StreamApplication

fun main(args: Array<String>) {
    runApplication<StreamApplication>(*args)
}
