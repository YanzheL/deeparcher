package com.hitnslab.dnssecurity.deeparcher.aggregator

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app")
class AppProperties(
        val input: ResourceSpec,
        val output: List<ResourceSpec>
) {

    data class ResourceSpec(
            val type: String,
            val path: String,
            var options: MutableMap<String, String> = mutableMapOf()
    )
}