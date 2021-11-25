package de.pschm.kafka.demo.delivery


import de.pschm.kafka.demo.core.event.CrudEventType
import de.pschm.kafka.demo.core.event.DomainEvent
import de.pschm.kafka.demo.core.util.NoArg
import de.pschm.kafka.demo.kafka.KafkaMessageProducer
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneOffset
import javax.persistence.PostPersist
import javax.persistence.PostRemove
import javax.persistence.PostUpdate

@Component
@NoArg
class DeliveryEventHandler(
    @Value("\${spring.kafka.topic.producer.delivery}") val topic: String,
    private val kafkaMessageProducer: KafkaMessageProducer,
) {
    private val eventVersion = 1
    private val log = KotlinLogging.logger { }

    @PostRemove
    fun postRemove(delivery: Delivery) {
        kafkaMessageProducer.send(topic, buildDeliveryDomainEvent(delivery.toDto(), CrudEventType.DELETED))
    }

    @PostUpdate
    fun postUpdate(delivery: Delivery) {
        kafkaMessageProducer.send(topic, buildDeliveryDomainEvent(delivery.toDto(), CrudEventType.UPDATED))
    }

    @PostPersist
    fun postPersist(delivery: Delivery) {
        kafkaMessageProducer.send(topic, buildDeliveryDomainEvent(delivery.toDto(), CrudEventType.CREATED))
    }

    private fun buildDeliveryDomainEvent(
        deliveryDto: DeliveryDto,
        crudEventType: CrudEventType
    ): DomainEvent<DeliveryDto> {
        if (deliveryDto.deliveryId == null)
            throw RuntimeException("DeliveryId should not be null!")

        return DomainEvent(
            deliveryDto,
            crudEventType.name,
            deliveryDto.deliveryId,
            eventVersion,
            OffsetDateTime.now(ZoneOffset.UTC).toString()
        )
    }
}