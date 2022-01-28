package com.example.eventdriven.domain.config.data

import com.example.eventdriven.domain.config.data.RetryConfigData
import com.example.eventdriven.domain.config.data.TwitterEventData
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(value = [
    RetryConfigData::class,
    TwitterEventData::class
])
class ConfigurationProperties