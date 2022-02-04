package com.example.eventdriven.adapter.out.kafka.config

import com.example.eventdriven.application.infrastructure.config.data.KafkaConfigData
import com.example.eventdriven.application.infrastructure.config.data.KafkaProducerData
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.clients.producer.ProducerConfig
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import java.io.Serializable

@Configuration
class KafkaProducerConfig<K : Serializable, V : SpecificRecordBase>(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaProducerData: KafkaProducerData
) {

    @Bean
    fun producerConfig(): Map<String, Any> {
        val props = hashMapOf<String, Any>()
        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = kafkaConfigData.bootstrapServers
        props[kafkaConfigData.schemaRegistryUrlKey] = kafkaConfigData.schemaRegistryUrl
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = kafkaProducerData.keySerializerClass
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = kafkaProducerData.valueSerializerClass
        props[ProducerConfig.BATCH_SIZE_CONFIG] = kafkaProducerData.batchSize * kafkaProducerData.batchSizeBoostFactor
        props[ProducerConfig.LINGER_MS_CONFIG] = kafkaProducerData.lingerMs
        props[ProducerConfig.COMPRESSION_TYPE_CONFIG] = kafkaProducerData.compressionType
        props[ProducerConfig.ACKS_CONFIG] = kafkaProducerData.acks
        props[ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG] = kafkaProducerData.requestTimeoutMs
        props[ProducerConfig.RETRIES_CONFIG] = kafkaProducerData.retryCount

        return props
    }

    @Bean
    fun producerFactory(): ProducerFactory<K, V> {
        return DefaultKafkaProducerFactory(producerConfig())
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<K, V> {
        return KafkaTemplate(producerFactory())
    }
}