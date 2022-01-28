package com.example.eventdriven.adapter.out.kafka.client

import com.example.eventdriven.application.infrastructure.config.data.KafkaConfigData
import org.apache.kafka.clients.admin.AdminClient
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component

@Component
class KafkaAdminClient(
    private val kafkaConfigData: KafkaConfigData,
    private val retryConfigData: KafkaConfigData,
    private val adminClient: AdminClient,
    private val retryTemplate: RetryTemplate
) {

}