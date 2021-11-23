package de.pschm.kafka.demo

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.pschm.kafka.demo.core.event.CrudEventType
import de.pschm.kafka.demo.core.event.DomainEvent
import de.pschm.kafka.demo.customer.CustomerDto
import org.apache.kafka.clients.consumer.ConsumerRecord
import java.time.OffsetDateTime
import java.time.ZoneOffset

object TestUtil {

    const val SAMPLE_ID = "3415baf0-0066-4bda-a75c-a3946a0b3df1"
    const val SAMPLE_NAME = "Peter"
    const val SAMPLE_TITLE = "Manager"
    const val SAMPLE_ADDRESS = "Weg 77, 12345 Paris"

    fun sampleCustomerDto() = CustomerDto(SAMPLE_ID, SAMPLE_NAME, SAMPLE_TITLE, SAMPLE_ADDRESS)
    fun sampleCustomerDomainEvent() = DomainEvent(
            sampleCustomerDto(),
            CrudEventType.CREATED.name,
            SAMPLE_ID,
            1,
            OffsetDateTime.now(ZoneOffset.UTC).toString(),
        )
    fun sampleSerializedCustomerDto(): String = jacksonObjectMapper().writeValueAsString(sampleCustomerDto())
    fun sampleCustomerConsumerRecord(): ConsumerRecord<String, String> {
        val event = sampleCustomerDomainEvent()
        val record = ConsumerRecord("customer", 0, 0, SAMPLE_ID, sampleSerializedCustomerDto())

        record.headers().add("eventId", event.id.toByteArray())
        record.headers().add("transactionId", event.id.toByteArray())
        record.headers().add("version", event.version.toString().toByteArray())
        record.headers().add("timestamp", event.timestamp.toByteArray())
        record.headers().add("type", event.id.toByteArray())

        return record
    }
    fun sampleInvalidCustomerConsumerRecord(): ConsumerRecord<String, String> {
        return ConsumerRecord("customer", 0, 0, SAMPLE_ID, sampleSerializedCustomerDto())
    }

}