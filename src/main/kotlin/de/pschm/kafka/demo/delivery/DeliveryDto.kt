package de.pschm.kafka.demo.delivery

import java.util.*

data class DeliveryDto(
    val deliveryId: String?,
    val item: String,
    val targetAddress: String
) {
    fun toEntity(): Delivery {
        if (deliveryId == null)
            return Delivery(item = item, targetAddress = targetAddress)

        return Delivery(deliveryId, item, targetAddress)
    }
}