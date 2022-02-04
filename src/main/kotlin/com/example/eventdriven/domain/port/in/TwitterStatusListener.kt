package com.example.eventdriven.domain.port.`in`

import twitter4j.Status

interface TwitterStatusListener {
    fun onStatus(status: Status)
}