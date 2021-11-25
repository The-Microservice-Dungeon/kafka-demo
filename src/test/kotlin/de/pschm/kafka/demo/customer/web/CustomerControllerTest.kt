package de.pschm.kafka.demo.customer.web

import de.pschm.kafka.demo.TestUtil
import de.pschm.kafka.demo.core.util.UriUtil
import de.pschm.kafka.demo.customer.CustomerDto
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.client.postForEntity
import org.springframework.http.HttpEntity
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerControllerTest {

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    @Test
    fun testCreate_CreatedCustomer_ReturnCreatedCustomer() {
        // given
        val customerDto = TestUtil.sampleCustomerDto()

        // when
        val httpEntity = HttpEntity(customerDto)
        val response = testRestTemplate.postForEntity<CustomerDto>(UriUtil.CUSTOMERS, httpEntity, CustomerDto::class)

        // then
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
    }
}