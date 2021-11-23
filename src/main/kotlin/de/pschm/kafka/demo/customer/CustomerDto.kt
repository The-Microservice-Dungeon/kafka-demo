package de.pschm.kafka.demo.customer

data class CustomerDto(
    val id: String,
    val name: String,
    val jobTitle: String,
    var address: String,
) {
    fun toEntity() = Customer(id, name, jobTitle, address)
}