package com.example.twitterservice.application.infrastructure.config

import com.example.twitterservice.application.infrastructure.config.data.RetryConfigData
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

@Configuration
class RetryConfig {

    @Bean
    fun retryTemplate(retryConfigData: RetryConfigData): RetryTemplate {
        val retryTemplate = RetryTemplate()

        val exponentialBackOffPolicy = ExponentialBackOffPolicy()
        with(exponentialBackOffPolicy) {
            initialInterval = retryConfigData.initialIntervalMs
            maxInterval = retryConfigData.maxIntervalMs
            multiplier = retryConfigData.multiplier
        }

        val simpleRetryPolicy = SimpleRetryPolicy()
        simpleRetryPolicy.maxAttempts = retryConfigData.maxAttempts

        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy)
        retryTemplate.setRetryPolicy(simpleRetryPolicy)

        return retryTemplate
    }
}