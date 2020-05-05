package com.hitnslab.dnssecurity.deeparcher.stream.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.graph")
data class GraphProperties(
    val input: AppProperties.ResourceSpec,
    val output: AppProperties.ResourceSpec
)