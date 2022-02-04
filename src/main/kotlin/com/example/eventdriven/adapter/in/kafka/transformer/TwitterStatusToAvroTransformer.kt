package com.example.eventdriven.adapter.`in`.kafka.transformer

import com.example.eventdriven.adapter.out.kafka.avro.TwitterAvroModel
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