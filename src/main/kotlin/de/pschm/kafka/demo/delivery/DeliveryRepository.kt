package de.pschm.kafka.demo.delivery

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface DeliveryRepository: CrudRepository<Delivery, String>