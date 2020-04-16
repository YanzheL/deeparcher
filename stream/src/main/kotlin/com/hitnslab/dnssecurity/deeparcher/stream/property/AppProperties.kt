package com.hitnslab.dnssecurity.deeparcher.stream.property

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app")
class AppProperties {

    data class ResourceSpec(
            val type: String,
            val path: String,
            var options: MutableMap<String, String> = mutableMapOf()
    )
}