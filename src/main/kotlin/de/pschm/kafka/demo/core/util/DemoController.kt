package de.pschm.kafka.demo.core.util

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.javafaker.Faker
import de.pschm.kafka.demo.core.event.CrudEventType
import de.pschm.kafka.demo.core.event.DomainEvent
import de.pschm.kafka.demo.core.event.EventErrorRepository
import de.pschm.kafka.demo.customer.CustomerDto
import de.pschm.kafka.demo.delivery.DeliveryDto
import de.pschm.kafka.demo.kafka.KafkaMessageProducer
import mu.KotlinLogging
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.http.ResponseEntity
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Controller
class DemoController(
    val kafkaMessageProducer: KafkaMessageProducer,
    val eventErrorRepository: EventErrorRepository,
    val kafkaTemplate: KafkaTemplate<String, String>
) {
    private val log = KotlinLogging.logger { }
    private val objectMapper = jacksonObjectMapper()
    private val deliveries = mutableListOf<DeliveryDto>()

    @RequestMapping(
        value = ["/demo/customer"],
        produces = ["application/json"],
        method = [RequestMethod.POST]
    )
    fun publishRandomUser(): ResponseEntity<String> {
        val faker = Faker.instance()
        val customerDto = CustomerDto(
            UUID.randomUUID().toString(),
            faker.dune().character(),
            faker.job().title(),
            faker.address().fullAddress()
        )
        val event = DomainEvent(customerDto, CrudEventType.CREATED.name, customerDto.id, 1, OffsetDateTime.now(ZoneOffset.UTC).toString())
        kafkaMessageProducer.send("customer", event)

        log.info { "Send demo customer ${CrudEventType.CREATED} event" }
        return ResponseEntity.ok(objectMapper.writeValueAsString(customerDto))
    }

    @RequestMapping(
        value = ["/demo/customer/error"],
        produces = ["application/json"],
        method = [RequestMethod.POST]
    )
    fun publishCustomerError(): ResponseEntity<String> {

        val record = ProducerRecord("customer", "event.id", "invalid-payload")
//        record.headers().add("eventId", event.id.toByteArray())
//        record.headers().add("transactionId", event.id.toByteArray())
//        record.headers().add("version", event.version.toString().toByteArray())
//        record.headers().add("timestamp", event.timestamp.toByteArray())
//        record.headers().add("type", event.id.toByteArray())

        val future = kafkaTemplate.send(record)
        return ResponseEntity.ok("Error generated")
    }

    @RequestMapping(
        value = ["/demo/deliveries"],
        produces = ["application/json"],
        method = [RequestMethod.GET]
    )
    fun getAllDeliveries(): ResponseEntity<String> {
        return ResponseEntity.ok(objectMapper.writeValueAsString(deliveries))
    }

    @RequestMapping(
        value = ["/demo/errors"],
        produces = ["application/json"],
        method = [RequestMethod.GET]
    )
    fun getAllEventingErrors(): ResponseEntity<String> {
        val errors = eventErrorRepository.findAll().toList()
        return ResponseEntity.ok(objectMapper.writeValueAsString(errors))
    }

    @KafkaListener(
        topics = ["delivery"],
        groupId = "\${spring.kafka.group-id}",
        autoStartup = "\${spring.kafka.enabled}",
    )
    fun listenToDeliveries(message: String) {
        // could read event metadata here
        val event = objectMapper.readValue(message, DeliveryDto::class.java)

        deliveries += event
        log.info { "Consumed demo delivery Event: $event" }
    }
}