package com.hitnslab.dnssecurity.pdnsdataloader

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PDNSDataLoaderApplication

fun main(args: Array<String>) {
    runApplication<PDNSDataLoaderApplication>(*args)
}
