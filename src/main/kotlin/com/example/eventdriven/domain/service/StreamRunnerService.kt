package com.example.eventdriven.domain.service

import com.example.eventdriven.domain.port.`in`.StreamRunnerUseCase
import com.example.eventdriven.domain.port.out.EventStreamRunnerOut
import org.springframework.stereotype.Service

@Service
class StreamRunnerService(
    private val eventStreamRunnerOut: EventStreamRunnerOut
) : StreamRunnerUseCase {

    override fun start() {
        eventStreamRunnerOut.start()
    }

}