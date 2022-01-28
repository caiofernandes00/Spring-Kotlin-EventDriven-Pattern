package com.example.eventdriven

import com.example.eventdriven.domain.port.`in`.StreamRunnerUseCase
import com.example.eventdriven.domain.config.data.TwitterEventData
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EventdrivenApplication(
    private val twitterEventData: TwitterEventData,
    private val streamRunnerUseCase: StreamRunnerUseCase,
) : CommandLineRunner {

    private var logger = LoggerFactory.getLogger(EventdrivenApplication::class.java)

    override fun run(vararg args: String?) {
        logger.info("App starts...")
        logger.info(arrayOf(twitterEventData.twitterKeywords).toString())
        logger.info(twitterEventData.welcomeMessage)
        streamRunnerUseCase.start()
    }
}

fun main(args: Array<String>) {
    runApplication<EventdrivenApplication>(*args)
}
