package com.example.eventdriven

import com.example.eventdriven.adapter.`in`.twitterevent.stream.EventStreamRunnerAdapter
import com.example.eventdriven.application.infrastructure.config.data.TwitterEventData
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventDrivenApplication(
    private val twitterEventData: TwitterEventData,
    private val eventStreamRunnerAdapter: EventStreamRunnerAdapter,
) : CommandLineRunner {

    private var logger = LoggerFactory.getLogger(EventDrivenApplication::class.java)

    override fun run(vararg args: String?) {
        logger.info("App starts...")
        logger.info(arrayOf(twitterEventData.twitterKeywords).toString())
        logger.info(twitterEventData.welcomeMessage)
        eventStreamRunnerAdapter.start()
    }
}

fun main(args: Array<String>) {
    runApplication<EventDrivenApplication>(*args)
}
