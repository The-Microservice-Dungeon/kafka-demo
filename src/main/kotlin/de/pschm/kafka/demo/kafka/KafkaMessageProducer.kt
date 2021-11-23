package de.pschm.kafka.demo.kafka

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import de.pschm.kafka.demo.core.event.DomainEvent
import de.pschm.kafka.demo.core.event.EventErrorRepository
import de.pschm.kafka.demo.core.event.KafkaError
import de.pschm.kafka.demo.core.util.BeanUtil
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class KafkaMessageProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val beanUtil: BeanUtil
) {
    private val log = KotlinLogging.logger { }
    private val objectMapper = jacksonObjectMapper()
    private val errors = mutableListOf<Pair<String, DomainEvent<*>>>()

    fun send(topic: String, event: DomainEvent<*>) {
        val record = ProducerRecord(topic, event.id, objectMapper.writeValueAsString(event.payload))
        record.headers().add("eventId", event.id.toByteArray())
        record.headers().add("transactionId", event.id.toByteArray())
        record.headers().add("version", event.version.toString().toByteArray())
        record.headers().add("timestamp", event.timestamp.toByteArray())
        record.headers().add("type", event.id.toByteArray())

        val future = kafkaTemplate.send(record)

        future.addCallback(
            {
                log.info { "Successfully send message with key: {${it?.producerRecord?.key()}}" }
            },
            {
                val errorMsg = "Couldn't send message: $record \n${it.message}"
                errors.add(Pair(topic, event))
                beanUtil.getBean(EventErrorRepository::class.java)?.save(KafkaError(msg = errorMsg))
                log.error { errorMsg }
            }
        )
    }

    @Scheduled(initialDelay = 30000L, fixedDelay = 15000)
    fun retryEvent() {
        val (topic, event) = errors.removeFirstOrNull() ?: return
        log.info { "Retry Event: $event" }
        send(topic, event)
    }
}