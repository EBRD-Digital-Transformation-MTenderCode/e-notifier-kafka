package com.procurement.notifierkafka.kafka

import com.procurement.notifierkafka.domain.Notification
import com.procurement.notifierkafka.repository.TopicRepository
import kotlinx.coroutines.experimental.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate

interface NotificationSender {
    suspend fun send(notification: Notification)
}

class NotificationSenderImpl(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val topicRepository: TopicRepository
) : NotificationSender {

    companion object {
        val log: Logger = LoggerFactory.getLogger(NotificationSenderImpl::class.java)
        private const val FIRST_DELAY: Long = 100L
        private const val MAX_DELAY: Long = 60000L
    }

    override suspend fun send(notification: Notification) {
        val topic = notification.getTopic()
        if (topic != null) {
            notification.send(topic)
        } else {
            log.error(
                "Error of send notification to topic of Kafka (topic for platform id: '${notification.platformId}' not found)."
            )
        }
    }

    //TODO Cache
    private suspend fun Notification.getTopic(): String? {
        var delayAmount = FIRST_DELAY
        while (true) {
            try {
                return topicRepository.getTopic(platformId)
            } catch (ex: Exception) {
                log.error("Error of get topic for platformId: '$platformId' from database.", ex)
            }

            delay(delayAmount)
            delayAmount = (delayAmount * 2).coerceAtMost(MAX_DELAY)
        }
    }

    private suspend fun Notification.send(topic: String) {
        var delayAmount = FIRST_DELAY
        while (true) {
            try {
                log.debug("Sending notification(platformId: '$platformId', operationId: '$operationId') to topic: '$topic'.")
                kafkaTemplate.send(topic, message).get()
                log.debug("Notification(platformId: '$platformId', operationId: '$operationId') was sent to topic: '$topic'.")
                return
            } catch (ex: Exception) {
                log.error(
                    "Error of sending notification(platformId: '$platformId', operationId: '$operationId') to topic: '$topic'.",
                    ex
                )
            }

            delay(delayAmount)
            delayAmount = (delayAmount * 2).coerceAtMost(MAX_DELAY)
        }
    }
}
