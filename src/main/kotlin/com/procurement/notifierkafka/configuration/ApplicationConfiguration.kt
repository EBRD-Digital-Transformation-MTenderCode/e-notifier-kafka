package com.procurement.notifierkafka.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

/**
 * The Java-configuration of application.
 */
@Configuration
@Import(
    value = [
        CassandraConfiguration::class,
        RepositoryConfiguration::class,
        KafkaConfiguration::class
    ]
)
class ApplicationConfiguration
