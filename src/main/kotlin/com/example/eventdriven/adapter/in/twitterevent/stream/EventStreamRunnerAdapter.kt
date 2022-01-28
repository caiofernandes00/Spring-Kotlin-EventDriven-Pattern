package com.example.eventdriven.adapter.`in`.twitterevent.stream

import com.example.eventdriven.adapter.`in`.twitterevent.listener.TwitterEventStatusListener
import com.example.eventdriven.domain.port.out.EventStreamRunnerOut
import com.example.eventdriven.domain.config.data.TwitterEventData
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import twitter4j.FilterQuery
import twitter4j.TwitterException
import twitter4j.TwitterStream
import twitter4j.TwitterStreamFactory

@Service
@ConditionalOnProperty(
    name = ["twitter-to-kafka-service.enable-mock-tweets"],
    havingValue = "false",
    matchIfMissing = true
)
class EventStreamRunnerAdapter(
    private val twitterEventStatusListener: TwitterEventStatusListener,
    private val twitterEventData: TwitterEventData,
) : EventStreamRunnerOut {

    private var logger =
        LoggerFactory.getLogger(EventStreamRunnerAdapter::class.java)
    private lateinit var twitterStream: TwitterStream

    @Throws(TwitterException::class)
    override fun start() {
        twitterStream = TwitterStreamFactory().instance
        twitterStream.addListener(twitterEventStatusListener)
        addFilter()
    }

    private fun addFilter() {
        val keywords = arrayOf(twitterEventData.twitterKeywords.toString())
        val filterQuery = FilterQuery(*keywords)
        twitterStream.filter(filterQuery)
        logger.info("Started filtering stream for keywords $keywords")
    }
}
