package com.example.eventdriven.application.infrastructure.config.data

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "twitter-event-config")
data class TwitterEventData(
    val twitterKeywords: List<String>,
    val welcomeMessage: String,
    val enableMockTweets: Boolean,
    val mockSleepMs: Long,
    val mockMinTweetLength: Int,
    val mockMaxTweetLength: Int
)