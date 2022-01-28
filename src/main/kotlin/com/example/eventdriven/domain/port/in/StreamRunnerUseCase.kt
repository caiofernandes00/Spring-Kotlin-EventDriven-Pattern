package com.example.eventdriven.domain.port.`in`

import twitter4j.TwitterException

interface StreamRunnerUseCase {
    @Throws(TwitterException::class)
    fun start()
}