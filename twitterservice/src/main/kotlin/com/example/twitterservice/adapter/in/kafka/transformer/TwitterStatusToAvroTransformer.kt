package com.example.twitterservice.adapter.`in`.kafka.transformer

import com.example.twitterservice.adapter.out.kafka.avro.TwitterAvroModel
import org.springframework.stereotype.Component
import twitter4j.Status

@Component
class TwitterStatusToAvroTransformer {
    fun transform(status: Status): TwitterAvroModel =
        TwitterAvroModel.newBuilder()
            .setId(status.id)
            .setUserId(status.user.id)
            .setText(status.text)
            .setCreatedAt(status.createdAt.time)
            .build()

}