package com.example.eventdriven.adapter.out.kafka.config

import com.example.eventdriven.application.infrastructure.config.data.KafkaConfigData
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.AdminClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@Configuration
class KafkaAdminConfig(
    private val kafkaConfigData: KafkaConfigData,
) {

    @Bean
    fun adminClient(): AdminClient = AdminClient.create(
        mutableMapOf(
            Pair<String, Any>(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, kafkaConfigData.bootstrapServers),
        )
    )

}