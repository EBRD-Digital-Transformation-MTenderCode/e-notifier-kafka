package com.procurement.notifierkafka.configuration.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.kafka.producer.security")
class KafkaSecurityProperties {
    /**
     * Username.
     */
    var username: String? = null

    /**
     *Password.
     */
    var password: String? = null
}