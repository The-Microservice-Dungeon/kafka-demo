package de.pschm.kafka.demo.core.event

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Repository
interface EventErrorRepository : CrudRepository<KafkaError, String>

@Entity
class KafkaError(
    @Id val id: String = UUID.randomUUID().toString(),
    val msg: String
)