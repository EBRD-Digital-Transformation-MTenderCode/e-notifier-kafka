package com.procurement.notifierkafka

import com.procurement.notifierkafka.configuration.ApplicationConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackageClasses = [ApplicationConfiguration::class])
class NotifierKafkaApplication

fun main(args: Array<String>) {
    runApplication<NotifierKafkaApplication>(*args)
}
