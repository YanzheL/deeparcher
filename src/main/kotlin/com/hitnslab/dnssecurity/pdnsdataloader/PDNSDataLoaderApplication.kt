package com.hitnslab.dnssecurity.pdnsdataloader

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties
class PDNSDataLoaderApplication

fun main(args: Array<String>) {
    runApplication<PDNSDataLoaderApplication>(*args)
}
