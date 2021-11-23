package de.pschm.kafka.demo.delivery

import org.hibernate.Hibernate
import java.util.*
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.Id

@Entity
@EntityListeners(DeliveryEventHandler::class)
class Delivery(
    @Id
    val deliveryId: String? = UUID.randomUUID().toString(),
    val item: String,
    val targetAddress: String
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Delivery

        return deliveryId == other.deliveryId
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(orderId = $deliveryId , item = $item , targetAddress = $targetAddress )"
    }

    fun toDto() = DeliveryDto(deliveryId, item, targetAddress)
}