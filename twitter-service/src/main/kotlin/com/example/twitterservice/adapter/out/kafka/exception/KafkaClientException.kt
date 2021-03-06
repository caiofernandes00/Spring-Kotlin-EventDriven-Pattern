package com.example.twitterservice.adapter.out.kafka.exception

class KafkaClientException : RuntimeException {
    constructor(): super()

    constructor(message: String): super(message)

    constructor(message: String, cause: Throwable): super(message, cause)

}