package com.procurement.notifierkafka.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.procurement.notifierkafka.configuration.properties.KafkaSecurityProperties
import com.procurement.notifierkafka.kafka.NotificationListener
import com.procurement.notifierkafka.kafka.NotificationSenderImpl
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.common.config.SaslConfigs
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

/**
 * The Java-configuration of Services.
 */
@Configuration
@Import(
    value = [
        RepositoryConfiguration::class
    ]
)
@EnableKafka
@EnableConfigurationProperties(KafkaSecurityProperties::class)
class KafkaConfiguration @Autowired constructor(
    private val kafkaProperties: KafkaProperties,
    private val kafkaSecurityProperties: KafkaSecurityProperties,
    private val mapper: ObjectMapper,
    private val repositoryConfiguration: RepositoryConfiguration
) {

    @Bean
    fun notificationListener() = NotificationListener(
        mapper = mapper,
        notificationSender = notificationSender()
    )

    @Bean
    fun platformKafkaTemplate(): KafkaTemplate<String, String> = KafkaTemplate(platformProducerFactory())

    @Bean
    fun platformProducerFactory(): ProducerFactory<String, String> {
        val configProps = kafkaProperties.buildProducerProperties()
        configProps.putAll(
            mapOf(
                CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to "SASL_PLAINTEXT",
                SaslConfigs.SASL_MECHANISM to "PLAIN",
                SaslConfigs.SASL_JAAS_CONFIG to kafkaSecurityProperties.sasl()
            )
        )
        return DefaultKafkaProducerFactory(configProps)
    }

    @Bean
    fun notificationSender() = NotificationSenderImpl(
        kafkaTemplate = platformKafkaTemplate(),
        topicRepository = repositoryConfiguration.topicRepository()
    )

    fun KafkaSecurityProperties.sasl(): String {
        return "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"$username\" password=\"$password\";"
    }
}
