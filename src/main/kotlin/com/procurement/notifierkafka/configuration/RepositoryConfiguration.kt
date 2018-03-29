package com.procurement.notifierkafka.configuration

import com.procurement.notifierkafka.repository.TopicRepository
import com.procurement.notifierkafka.repository.TopicRepositoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(CassandraConfiguration::class)
class RepositoryConfiguration(private val cassandraConfiguration: CassandraConfiguration) {
    @Bean
    fun topicRepository(): TopicRepository = TopicRepositoryImpl(cassandraConfiguration.session())
}
