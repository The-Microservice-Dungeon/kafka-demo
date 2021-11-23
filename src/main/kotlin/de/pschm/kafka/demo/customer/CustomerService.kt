package de.pschm.kafka.demo.customer

import org.springframework.stereotype.Service

@Service
class CustomerService(
    val repository: CustomerRepository
) {
    fun save(customerDto: CustomerDto): CustomerDto {
        val entity = repository.save(customerDto.toEntity())
        return entity.toDto()
    }

    fun findAll(): List<CustomerDto> {
        return repository.findAll().map { it.toDto() }
    }
}