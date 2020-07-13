package com.hitnslab.dnssecurity.deeparcher.stream.property

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "app.graph-builder")
data class GraphProperties(
    val input: AppProperties.ResourceSpec,
    val output: AppProperties.ResourceSpec,
    val nodeIdService: NodeIdService
) {
    data class NodeIdService(
        val uri: String,
        val database: String,
        val collection: String,
        val keyField: String,
        val valueField: String
    )
}