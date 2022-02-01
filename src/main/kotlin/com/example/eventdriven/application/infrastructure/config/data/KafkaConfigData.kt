package com.example.eventdriven.application.infrastructure.config.data

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "kafka-config")
data class KafkaConfigData(
    val bootstrapServers: String,
    val schemaRegistryUrlKey: String,
    val schemaRegistryUrl: String,
    val topicName: String,
    val topicNameToCreate: List<String>,
    val numOfPartitions: Int,
    val replicationFactor: Short,
)