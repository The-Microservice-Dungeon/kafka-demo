package de.pschm.kafka.demo.core.event

import org.apache.kafka.common.header.Headers
import java.util.*

data class DomainEvent<T>(
    val payload: T,
    val type: String,
    val key: String,
    val version: Int,
    val timestamp: String,
    val id: String = UUID.randomUUID().toString()
) {
    companion object {
        fun <T> build(payload: T, headers: Headers): DomainEvent<T> {
            val eventId = String(headers.lastHeader("eventId").value())
            val transactionId = String(headers.lastHeader("transactionId").value())
            val version = String(headers.lastHeader("version").value())
            val timestamp = String(headers.lastHeader("timestamp").value())
            val type = String(headers.lastHeader("type").value())

            return DomainEvent(payload, type, transactionId, version.toIntOrNull() ?: 1, timestamp, eventId)
        }
    }
}

enum class CrudEventType {
    CREATED, UPDATED, DELETED
}