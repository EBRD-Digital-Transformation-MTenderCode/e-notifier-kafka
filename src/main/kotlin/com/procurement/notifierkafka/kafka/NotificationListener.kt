package com.procurement.notifierkafka.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.notifierkafka.domain.Notification
import com.procurement.notifierkafka.exception.ParseNotificationException
import kotlinx.coroutines.experimental.runBlocking
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment

class NotificationListener(
    private val mapper: ObjectMapper,
    private val notificationSender: NotificationSender
) {
    companion object {
        val log: Logger = LoggerFactory.getLogger(NotificationListener::class.java)
    }

    @KafkaListener(
        topics = ["notification-kafka-channel"],
        containerFactory = "kafkaListenerContainerFactory"
    )
    fun listenToNotification(message: String, ack: Acknowledgment) {
        try {
            val notification = parse(message)
            runBlocking {
                notificationSender.send(notification)
            }
            ack.acknowledge()
        } catch (ex: Exception) {
            log.error("Error of processing notification.", ex)
        }
    }

    private fun parse(message: String): Notification = try {
        mapper.readValue(message, Notification::class.java)
    } catch (ex: Exception) {
        throw ParseNotificationException(message)
    }
}
