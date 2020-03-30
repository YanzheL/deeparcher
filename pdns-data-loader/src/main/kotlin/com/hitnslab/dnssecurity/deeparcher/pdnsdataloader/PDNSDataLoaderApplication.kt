package com.hitnslab.dnssecurity.deeparcher.pdnsdataloader

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(proxyBeanMethods = false)
class PDNSDataLoaderApplication

fun main(args: Array<String>) {
    runApplication<PDNSDataLoaderApplication>(*args)
}
