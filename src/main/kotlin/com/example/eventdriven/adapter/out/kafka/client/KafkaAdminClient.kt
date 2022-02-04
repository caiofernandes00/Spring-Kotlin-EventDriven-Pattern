package com.example.eventdriven.adapter.out.kafka.client

import com.example.eventdriven.adapter.out.kafka.exception.KafkaClientException
import com.example.eventdriven.application.infrastructure.config.data.KafkaConfigData
import com.example.eventdriven.application.infrastructure.config.data.RetryConfigData
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.CreateTopicsResult
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.admin.TopicListing
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.retry.RetryContext
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.util.concurrent.ExecutionException


@Component
class KafkaAdminClient(
    private val kafkaConfigData: KafkaConfigData,
    private val retryConfigData: RetryConfigData,
    private val adminClient: AdminClient,
    private val retryTemplate: RetryTemplate,
    private val webClient: WebClient
) {

    private val logger = LoggerFactory.getLogger(KafkaAdminClient::class.java)

    fun createTopics() {
        try {
            val createTopicsResult: CreateTopicsResult =
                retryTemplate.execute<CreateTopicsResult, RuntimeException>(::doCreateTopics)
            logger.info("Create topic result {}", createTopicsResult.values().values)
        } catch (t: Throwable) {
            throw KafkaClientException("Reached max number of retry for creating kafka topic(s)!", t)
        }

        checkTopicsCreated()
    }

    fun checkTopicsCreated() {
        var topics = getTopics()
        var retryCount = 1
        val maxRetry = retryConfigData.maxAttempts
        val multiplier = retryConfigData.multiplier
        var sleepTimeMs = retryConfigData.sleepTimeMs

        for (topic in kafkaConfigData.topicNameToCreate) {
            while (!isTopicCreated(topics, topic)) {
                checkMaxRetry(retryCount++, maxRetry)
                sleep(sleepTimeMs)
                sleepTimeMs *= multiplier.toLong()
                topics = getTopics()
            }
        }
    }

    fun checkSchemaRegistry() {
        var retryCount = 1
        val maxRetry = retryConfigData.maxAttempts
        val multiplier = retryConfigData.multiplier
        var sleepTimeMs = retryConfigData.sleepTimeMs
        while (!getSchemaRegistryStatus().is2xxSuccessful) {
            checkMaxRetry(retryCount++, maxRetry)
            sleep(sleepTimeMs)
            sleepTimeMs *= multiplier.toLong()
        }
    }

    private fun getTopics(): Collection<TopicListing> {
        try {
            return retryTemplate.execute<Collection<TopicListing>, RuntimeException>(::doGetTopics)
        } catch (t: Throwable) {
            throw KafkaClientException("Reached max number of retry for reading kafka topic(s)!", t)
        }
    }

    @Throws(ExecutionException::class, InterruptedException::class)
    private fun doGetTopics(retryContext: RetryContext): Collection<TopicListing> {
        logger.info("Reading kafka topic ${kafkaConfigData.topicNameToCreate}, attempt ${retryContext.retryCount}")
        val topics = adminClient.listTopics().listings().get()
        topics?.forEach { topic -> logger.info("Topic: ${topic.name()}") }

        return topics
    }

    private fun doCreateTopics(retryContext: RetryContext): CreateTopicsResult {
        val topicNames = kafkaConfigData.topicNameToCreate
        logger.info("Creating ${topicNames.size} topic(s), attempt #${retryContext.retryCount}")
        val kafkaTopics = topicNames.map { topic ->
            NewTopic(
                topic.trim { it <= ' ' },
                kafkaConfigData.numOfPartitions,
                kafkaConfigData.replicationFactor
            )
        }

        return adminClient.createTopics(kafkaTopics)
    }

    @Throws(KafkaClientException::class)
    private fun sleep(sleepTimeMs: Long) {
        try {
            Thread.sleep(sleepTimeMs)
        } catch (e: InterruptedException) {
            throw KafkaClientException("Error while sleeping for waiting new created topics!!")
        }
    }

    @Throws(KafkaClientException::class)
    private fun checkMaxRetry(retryCount: Int, maxRetry: Int) {
        if (retryCount > maxRetry) {
            throw KafkaClientException("Reached max number of retry for reading kafka topic(s)!!")
        }
    }

    private fun isTopicCreated(topics: Collection<TopicListing>?, topicName: String): Boolean =
        topics?.stream()?.anyMatch { it.name().equals(topicName) } ?: false

    private fun getSchemaRegistryStatus(): HttpStatus {
        return try {
            var httpStatus: HttpStatus = HttpStatus.SERVICE_UNAVAILABLE
            webClient
                .method(HttpMethod.GET)
                .uri(kafkaConfigData.schemaRegistryUrl)
                .exchangeToMono { e ->
                    httpStatus = e.statusCode()
                    e.bodyToMono(Any::class.java)
                }.block()

            httpStatus
        } catch (e: Exception) {
            HttpStatus.SERVICE_UNAVAILABLE
        }
    }
}