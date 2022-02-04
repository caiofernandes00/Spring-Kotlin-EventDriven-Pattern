package com.example.eventdriven

import com.example.eventdriven.adapter.`in`.twitterevent.stream.EventStreamRunnerAdapter
import com.example.eventdriven.application.infrastructure.config.data.TwitterEventData
import com.example.eventdriven.domain.port.`in`.BrokerStreamInitializer
import com.example.eventdriven.domain.port.`in`.TwitterStreamRunner
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventDrivenApplication(
    private val twitterStreamRunner: TwitterStreamRunner,
    private val brokerStreamInitializer: BrokerStreamInitializer
) : CommandLineRunner {

    private var logger = LoggerFactory.getLogger(EventDrivenApplication::class.java)

    override fun run(vararg args: String?) {
        logger.info("App starts...")
        brokerStreamInitializer.init()
        twitterStreamRunner.start()
    }
}

fun main(args: Array<String>) {
    runApplication<EventDrivenApplication>(*args)
}
