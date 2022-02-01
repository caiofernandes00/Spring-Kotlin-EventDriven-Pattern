package com.example.eventdriven.application.infrastructure.config.data

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    value = [
        RetryConfigData::class,
        TwitterEventData::class,
        KafkaConfigData::class
    ]
)
class ConfigurationProperties