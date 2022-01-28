package com.example.eventdriven.adapter.`in`.twitterevent.listener

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import twitter4j.Status
import twitter4j.StatusAdapter

@Component
class TwitterEventStatusListener : StatusAdapter() {

    private var logger = LoggerFactory.getLogger(TwitterEventStatusListener::class.java)

    override fun onStatus(status: Status) {
        logger.info("Twitter status with text {}", status.text)
    }
}