package de.pschm.kafka.demo.customer

import de.pschm.kafka.demo.TestUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerEventConsumerTest {

    @Autowired
    lateinit var customerEventConsumer: CustomerEventConsumer

    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Test
    fun testConsume_PersistValidCustomer_PersistedConsumedCustomer() {
        val event = TestUtil.sampleCustomerDomainEvent()
        val record = TestUtil.sampleCustomerConsumerRecord()

        customerEventConsumer.listen(record)

        val entity = customerRepository.findById(event.payload.id)
        assertThat(entity.isPresent).isTrue
    }

    @Test
    fun testConsume_PersistInvalidEvent_ThrownException() {
        val event = TestUtil.sampleCustomerDomainEvent()
        val record = TestUtil.sampleInvalidCustomerConsumerRecord()

        customerEventConsumer.listen(record)
        val entity = customerRepository.findById(event.payload.id)

        assertThat(entity.isEmpty).isTrue
    }
}