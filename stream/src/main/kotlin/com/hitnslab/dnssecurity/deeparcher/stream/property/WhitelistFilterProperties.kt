package com.hitnslab.dnssecurity.deeparcher.stream.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.whitelist-filter")
data class WhitelistFilterProperties(
        val whitelists: List<AppProperties.ResourceSpec>,
        val input: AppProperties.ResourceSpec,
        val output: Output,
        var prefilters: List<PrefilterSpec>? = null
) {
    data class Output(
            val match: List<AppProperties.ResourceSpec>,
            val miss: List<AppProperties.ResourceSpec>
    )

    data class PrefilterSpec(
            val field: String,
            val pattern: String,
            val allow: Boolean
    )
}