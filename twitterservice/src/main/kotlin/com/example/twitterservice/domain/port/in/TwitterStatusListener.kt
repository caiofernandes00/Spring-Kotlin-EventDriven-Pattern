package com.example.twitterservice.domain.port.`in`

import twitter4j.Status

interface TwitterStatusListener {
    fun onStatus(status: Status)
}