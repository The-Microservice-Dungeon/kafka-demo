package de.pschm.kafka.demo.delivery

import de.pschm.kafka.demo.core.exception.EntityIdMustBeNullException
import org.springframework.stereotype.Service

@Service
class DeliveryService(
    val repository: DeliveryRepository
) {
    fun create(deliveryDto: DeliveryDto): DeliveryDto {
        if (deliveryDto.deliveryId != null)
            throw EntityIdMustBeNullException(deliveryDto.toString())

        val entity = repository.save(deliveryDto.toEntity())
        return entity.toDto()
    }

    fun findAll() = repository.findAll().map { it.toDto() }
}