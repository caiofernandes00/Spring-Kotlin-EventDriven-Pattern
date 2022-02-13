package com.example.twitterservice

import com.example.twitterservice.domain.port.`in`.BrokerStreamInitializer
import com.example.twitterservice.domain.port.`in`.TwitterStreamRunner
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableAutoConfiguration
class TwitterServiceApplication(
    private val twitterStreamRunner: TwitterStreamRunner,
    private val brokerStreamInitializer: BrokerStreamInitializer
) : CommandLineRunner {

    private var logger = LoggerFactory.getLogger(TwitterServiceApplication::class.java)

    override fun run(vararg args: String?) {
        logger.info("App starts...")
        brokerStreamInitializer.init()
        twitterStreamRunner.start()
    }
}

fun main(args: Array<String>) {
    runApplication<TwitterServiceApplication>(*args)
}
