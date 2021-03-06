package com.example.twitterservice.adapter.out.kafka.producer

import com.example.twitterservice.adapter.out.kafka.avro.TwitterAvroModel
import com.example.twitterservice.domain.port.out.MessageProducerOut
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import javax.annotation.PreDestroy


@Service
class KafkaMessageProducerAdapter(
    private val kafkaTemplate: KafkaTemplate<Long, TwitterAvroModel>
) : MessageProducerOut<Long, TwitterAvroModel> {

    private val logger = LoggerFactory.getLogger(KafkaMessageProducerAdapter::class.java)

    override fun send(topic: String, key: Long, value: TwitterAvroModel) {
        logger.info("Sending message: $value to topic: $topic")
        val future: ListenableFuture<*> = kafkaTemplate.send(topic, key, value)
        addCallback(future)
    }

    @PreDestroy
    fun close() {
        logger.info("Closing KafkaTemplate")
        kafkaTemplate.destroy()
    }

    private fun addCallback(future: ListenableFuture<*>) {
        future.addCallback(object : ListenableFutureCallback<Any> {
            override fun onSuccess(result: Any?) {
                result as SendResult<*, *>?
                val metadata = result?.recordMetadata

                logger.info(
                    "Received new metadata. Topic: ${metadata?.topic()}, " +
                            "Partition: ${metadata?.partition()}, " +
                            "Offset: ${metadata?.offset()} " +
                            "Timestamp: ${metadata?.timestamp()} " +
                            "At time ${System.nanoTime()}"
                )
            }

            override fun onFailure(ex: Throwable) {
                logger.error("Message sending failed", ex)
            }
        })
    }
}