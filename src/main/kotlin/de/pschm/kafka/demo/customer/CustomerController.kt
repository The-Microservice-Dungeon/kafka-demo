package de.pschm.kafka.demo.customer

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
class CustomerController(
    val service: CustomerService
) {
    private val log = KotlinLogging.logger { }

    @PostMapping(UriUtil.CUSTOMERS)
    fun create(@Valid @RequestBody customerDto: CustomerDto): ResponseEntity<CustomerDto> {
        log.info { "POST new customer: $customerDto" }
        val result = service.save(customerDto)
        return ResponseEntity(result, HttpStatus.CREATED)
    }

    @GetMapping(UriUtil.CUSTOMERS)
    fun findAll(): ResponseEntity<List<CustomerDto>> {
        return ResponseEntity(service.findAll(), HttpStatus.OK)
    }
}