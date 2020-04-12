package com.hitnslab.dnssecurity.deeparcher.aggregator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(proxyBeanMethods = false)
class AggregatorApplication

fun main(args: Array<String>) {
    runApplication<AggregatorApplication>(*args)
}
