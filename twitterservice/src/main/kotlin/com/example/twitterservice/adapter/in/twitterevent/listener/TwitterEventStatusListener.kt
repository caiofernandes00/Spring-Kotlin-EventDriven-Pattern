package com.example.twitterservice.adapter.`in`.twitterevent.listener

import com.example.twitterservice.adapter.`in`.kafka.transformer.TwitterStatusToAvroTransformer
import com.example.twitterservice.adapter.out.kafka.avro.TwitterAvroModel
import com.example.twitterservice.application.infrastructure.config.data.KafkaConfigData
import com.example.twitterservice.domain.port.`in`.TwitterStatusListener
import com.example.twitterservice.domain.port.out.MessageProducerOut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import twitter4j.Status
import twitter4j.StatusAdapter

@Component
class TwitterEventStatusListener(
    private val twitterStatusToAvroTransformer: TwitterStatusToAvroTransformer,
    private val kafkaConfigData: KafkaConfigData,
    private val messageProducerOut: MessageProducerOut<Long, TwitterAvroModel>
) : StatusAdapter(), TwitterStatusListener {

    private var logger = LoggerFactory.getLogger(TwitterEventStatusListener::class.java)

    override fun onStatus(status: Status) {
        logger.info("Twitter status with text {}", status.text)
        val twitterAvroModel = twitterStatusToAvroTransformer.transform(status)
        messageProducerOut.send(kafkaConfigData.topicName, twitterAvroModel.userId, twitterAvroModel)
    }
}