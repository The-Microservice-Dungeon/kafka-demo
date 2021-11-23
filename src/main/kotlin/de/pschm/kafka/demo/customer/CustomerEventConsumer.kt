package de.pschm.kafka.demo.customer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.pschm.kafka.demo.core.event.DomainEvent
import de.pschm.kafka.demo.core.event.EventErrorRepository
import de.pschm.kafka.demo.core.event.KafkaError
import mu.KotlinLogging
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class CustomerEventConsumer(
    private val customerService: CustomerService,
    private val eventErrorRepository: EventErrorRepository
) {
    private val log = KotlinLogging.logger { }
    private val objectMapper = jacksonObjectMapper()

    @KafkaListener(
        topics = ["\${spring.kafka.topic.consumer.customer}"],
        groupId = "\${spring.kafka.group-id}",
        autoStartup = "\${spring.kafka.enabled}",
    )
    fun listen(record: ConsumerRecord<String, String>) {
        try {
            val payload = objectMapper.readValue(record.value(), CustomerDto::class.java)
            val event = DomainEvent.build(payload, record.headers())

            customerService.save(payload)
            log.info { "Consumed ${event.type} Event: $payload" }
        } catch (e: Exception) {
            val errorMsg = "Error while consuming customer event: $record \n ${e.stackTraceToString()}"
            val msg = record.value() + e.message
            eventErrorRepository.save(KafkaError(msg = msg))
            log.error { errorMsg }
        }
    }
}