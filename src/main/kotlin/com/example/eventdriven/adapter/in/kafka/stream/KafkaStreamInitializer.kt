package com.example.eventdriven.adapter.`in`.kafka.stream

import com.example.eventdriven.adapter.out.kafka.client.KafkaAdminClient
import com.example.eventdriven.application.infrastructure.config.data.KafkaConfigData
import com.example.eventdriven.domain.port.`in`.BrokerStreamInitializer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class KafkaStreamInitializer(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaAdminClient: KafkaAdminClient
) : BrokerStreamInitializer {

    private val log = LoggerFactory.getLogger(KafkaStreamInitializer::class.java)

    override fun init() {
        kafkaAdminClient.createTopics()
        kafkaAdminClient.checkSchemaRegistry()
        log.info("Topics with name ${kafkaConfigData.topicNameToCreate} is ready for operations!")
    }
}