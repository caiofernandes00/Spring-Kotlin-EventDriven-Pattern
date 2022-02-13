package com.example.twitterservice.application.infrastructure.config.data

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "retry-config")
data class RetryConfigData(
    val initialIntervalMs: Long,
    val maxIntervalMs: Long,
    val multiplier: Double,
    val maxAttempts: Int,
    val sleepTimeMs: Long
)
