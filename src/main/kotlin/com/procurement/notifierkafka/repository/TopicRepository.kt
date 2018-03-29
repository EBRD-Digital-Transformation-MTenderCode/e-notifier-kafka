package com.procurement.notifierkafka.repository

import com.datastax.driver.core.Session
import com.datastax.driver.core.querybuilder.QueryBuilder
import java.util.*

interface TopicRepository {
    fun getTopic(platformId: UUID): String?
}

class TopicRepositoryImpl(private val session: Session) : TopicRepository {
    companion object {
        private const val KEY_SPACE = "ocds"
        private const val TOPICS_TABLE = "notifier_kafka_topic"
        private const val PLATFORM_ID_FIELD = "platform_id"
        private const val TOPIC_ID_FIELD = "topic_id"
    }

    override fun getTopic(platformId: UUID): String? {
        val select = QueryBuilder.select()
            .from(KEY_SPACE, TOPICS_TABLE)
            .where(QueryBuilder.eq(PLATFORM_ID_FIELD, platformId))

        return session.execute(select)
            .one()?.getString(TOPIC_ID_FIELD)
    }
}
