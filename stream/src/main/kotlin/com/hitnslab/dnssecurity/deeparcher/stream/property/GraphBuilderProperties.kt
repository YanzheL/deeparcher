package com.hitnslab.dnssecurity.deeparcher.stream.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.graph-builder")
data class GraphBuilderProperties(
    val input: AppProperties.ResourceSpec,
    val output: AppProperties.ResourceSpec,
    val nodeIdService: NodeIdService,
    val cacheLimit: Long = 1000000L,
    val commitInterval: Long = 30
) {
    data class NodeIdService(
        val database: String,
        val collection: String,
        val keyField: String,
        val valueField: String
    )
}