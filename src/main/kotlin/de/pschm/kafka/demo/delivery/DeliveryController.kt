package de.pschm.kafka.demo.delivery

import de.pschm.kafka.demo.core.util.UriUtil
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import javax.validation.Valid

@Controller
class DeliveryController(
    val service: DeliveryService
) {
    private val log = KotlinLogging.logger { }

    @PostMapping(value = [UriUtil.DELIVERIES], consumes = ["application/json"], produces = ["application/json"])
    fun create(@Valid @RequestBody deliveryDto: DeliveryDto): ResponseEntity<DeliveryDto> {
        log.info { "POST new delivery: $deliveryDto" }
        val result = service.create(deliveryDto)
        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @GetMapping(value = [UriUtil.DELIVERIES], produces = ["application/json"])
    fun findAll(): ResponseEntity<List<DeliveryDto>> {
        return ResponseEntity(service.findAll(), HttpStatus.OK)
    }
}