package com.hitnslab.dnssecurity.deeparcher.whitelistfilter

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app")
class AppProperties(
        val whitelists: List<ResourceSpec>,
        val input: ResourceSpec,
        val output: Output,
        var prefilters: List<PrefilterSpec>? = null
) {

    data class Output(
            val match: List<ResourceSpec>,
            val miss: List<ResourceSpec>
    )

    data class PrefilterSpec(
            val field: String,
            val pattern: String,
            val allow: Boolean
    )

    data class ResourceSpec(
            val type: String,
            val path: String,
            var options: MutableMap<String, String> = mutableMapOf()
    )
}