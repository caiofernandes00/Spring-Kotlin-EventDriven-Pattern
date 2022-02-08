package com.example.eventdriven.adapter.`in`.twitterevent.listener

import com.example.eventdriven.adapter.`in`.kafka.transformer.TwitterStatusToAvroTransformer
import com.example.eventdriven.adapter.out.kafka.producer.KafkaMessageProducerAdapter
import com.example.eventdriven.application.infrastructure.config.data.KafkaConfigData
import com.example.eventdriven.domain.port.`in`.TwitterStatusListener
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import twitter4j.Status
import twitter4j.StatusAdapter

@Component
class TwitterEventStatusListener(
    private val twitterStatusToAvroTransformer: TwitterStatusToAvroTransformer,
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaProducer: KafkaMessageProducerAdapter
) : StatusAdapter(), TwitterStatusListener {

    private var logger = LoggerFactory.getLogger(TwitterEventStatusListener::class.java)

    override fun onStatus(status: Status) {
        logger.info("Twitter status with text {}", status.text)
        val twitterAvroModel = twitterStatusToAvroTransformer.transform(status)
        kafkaProducer.send(kafkaConfigData.topicName, twitterAvroModel.userId, twitterAvroModel)
    }
}