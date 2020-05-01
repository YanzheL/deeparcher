package com.hitnslab.dnssecurity.deeparcher.stream.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.aggregator")
data class AggregatorProperties(
    val input: AppProperties.ResourceSpec,
    val output: List<AppProperties.ResourceSpec>,
    val lookupTimeout: Long = 100L
)