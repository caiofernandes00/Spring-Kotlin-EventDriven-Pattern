package com.example.eventdriven.adapter.`in`.twitterevent.stream.fake

import com.example.eventdriven.adapter.`in`.twitterevent.listener.TwitterEventStatusListener
import com.example.eventdriven.application.infrastructure.config.data.TwitterEventData
import com.example.eventdriven.application.exception.TwitterEventException
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service
import twitter4j.Status
import twitter4j.TwitterException
import twitter4j.TwitterObjectFactory
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom

@Service
@ConditionalOnProperty(name = ["twitter-to-kafka-service.enable-mock-tweets"], havingValue = "true")
class FakeEventStreamRunnerAdapter(
    private val twitterEventStatusListener: TwitterEventStatusListener,
    private val twitterEventData: TwitterEventData
)  {


    private val logger = LoggerFactory.getLogger(FakeEventStreamRunnerAdapter::class.java)

    private val random: Random = Random()
    private val words = arrayOf(
        "Lorem",
        "ipsum",
        "dolor",
        "sit",
        "amet",
        "purus"
    )
    private val tweetAsRawJson = "{" +
            "\"created_at\":\"{0}\"," +
            "\"id\":\"{1}\"," +
            "\"text\":\"{2}\"," +
            "\"user\":{\"id\":\"{3}\"}" +
            "}"
    private val twitterStatusDateFormat = "EEE MMM dd HH:mm:ss zzz yyyy"


    @Throws(TwitterException::class)
    fun start() {
        val keywords = arrayOf(twitterEventData.twitterKeywords.toString())
        val minTweetsLength = twitterEventData.mockMinTweetLength
        val maxTweetsLength = twitterEventData.mockMaxTweetLength
        val sleepTimeMs = twitterEventData.mockSleepMs

        logger.info("Starting mock filtering twitter stream for keywords $keywords")
        simulateTwitterStream(keywords, minTweetsLength, maxTweetsLength, sleepTimeMs)
    }

    private fun simulateTwitterStream(
        keywords: Array<String>,
        minTweetLength: Int,
        maxTweetLength: Int,
        sleepTimeMs: Long,
    ) {
        Executors.newSingleThreadExecutor().submit {
            try {
                while (true) {
                    val formattedTweetAsRawJson = getFormattedTweet(keywords, minTweetLength, maxTweetLength)
                    val status: Status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson)
                    twitterEventStatusListener.onStatus(status)
                    sleep(sleepTimeMs)
                }
            } catch (e: TwitterException) {
                logger.error("Error creating twitter status!", e)
            }
        }
    }

    private fun sleep(sleepTimeMs: Long) {
        try {
            Thread.sleep(sleepTimeMs)
        } catch (e: InterruptedException) {
            throw TwitterEventException("Error while sleeping for waiting new status to create!!")
        }
    }

    private fun getFormattedTweet(keywords: Array<String>, minTweetLength: Int, maxTweetLength: Int): String {
        val params = arrayOf(
            ZonedDateTime.now().format(DateTimeFormatter.ofPattern(twitterStatusDateFormat, Locale.ENGLISH)),
            java.lang.String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE)),
            getRandomTweetContent(keywords, minTweetLength, maxTweetLength),
            java.lang.String.valueOf(ThreadLocalRandom.current().nextLong(Long.MAX_VALUE))
        )
        return formatTweetAsJsonWithParams(params)
    }

    private fun formatTweetAsJsonWithParams(params: Array<String>): String {
        var tweet = tweetAsRawJson
        for (i in params.indices) {
            tweet = tweet.replace("{$i}", params[i])
        }
        return tweet
    }

    private fun getRandomTweetContent(keywords: Array<String>, minTweetLength: Int, maxTweetLength: Int): String {
        val tweet = StringBuilder()
        val tweetLength: Int = random.nextInt(maxTweetLength - minTweetLength + 1) + minTweetLength
        return constructRandomTweet(keywords, tweet, tweetLength)
    }

    private fun constructRandomTweet(keywords: Array<String>, tweet: StringBuilder, tweetLength: Int): String {
        for (i in 0 until tweetLength) {
            tweet.append(words[random.nextInt(words.size)]).append(" ")
            if (i == tweetLength / 2) {
                tweet.append(keywords[random.nextInt(keywords.size)]).append(" ")
            }
        }
        return tweet.toString().trim { it <= ' ' }
    }
}