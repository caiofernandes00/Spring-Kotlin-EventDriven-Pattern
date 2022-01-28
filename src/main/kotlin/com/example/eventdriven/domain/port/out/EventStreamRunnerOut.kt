package com.example.eventdriven.domain.port.out

import twitter4j.TwitterException

interface EventStreamRunnerOut {
    @Throws(TwitterException::class)
    fun start()
}