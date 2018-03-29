package com.procurement.notifierkafka.repository

import com.datastax.driver.core.ResultSet
import com.datastax.driver.core.Row
import com.datastax.driver.core.Session
import com.datastax.driver.core.Statement
import com.nhaarman.mockito_kotlin.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.*

class TopicRepositoryTest {
    companion object {
        private val PLATFORM_ID = UUID.randomUUID()
        private val TOPIC_ID = UUID.randomUUID()
    }

    private lateinit var session: Session
    private lateinit var topicRepository: TopicRepository

    @BeforeEach
    fun init() {
        session = mock()
        topicRepository = TopicRepositoryImpl(session)
    }

    @Test
    @DisplayName("Testing the method getTopic.")
    fun getTopic() {
        val row = mock<Row>()
        val resultSet: ResultSet = mock()

        whenever(session.execute(any<Statement>()))
            .thenReturn(resultSet)
        whenever(resultSet.one())
            .thenReturn(row)
        whenever(row.getString("platform_id"))
            .thenReturn(PLATFORM_ID.toString())
        whenever(row.getString("topic_id"))
            .thenReturn(TOPIC_ID.toString())

        val result: String? = topicRepository.getTopic(PLATFORM_ID)

        assertNotNull(result)
        assertEquals(TOPIC_ID.toString(), result)

        val selectCapture = argumentCaptor<Statement>()
        verify(session, times(1))
            .execute(selectCapture.capture())
        getOperationCheckSql(selectCapture)
    }

    @Test
    @DisplayName("Testing the method getTopic(topic not found).")
    fun getTopic2() {
        val resultSet: ResultSet = mock()

        whenever(session.execute(any<Statement>()))
            .thenReturn(resultSet)
        whenever(resultSet.one())
            .thenReturn(null)

        val result: String? = topicRepository.getTopic(PLATFORM_ID)

        assertNull(result)

        val selectCapture = argumentCaptor<Statement>()
        verify(session, times(1))
            .execute(selectCapture.capture())
        getOperationCheckSql(selectCapture)
    }

    private fun getOperationCheckSql(selectCapture: KArgumentCaptor<Statement>) {
        val template = "SELECT * FROM ocds.notifier_kafka_topic WHERE platform_id=$PLATFORM_ID;"
        assertEquals(template, selectCapture.firstValue.toString())
    }
}