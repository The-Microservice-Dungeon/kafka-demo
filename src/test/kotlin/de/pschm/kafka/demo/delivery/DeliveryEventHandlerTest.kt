package de.pschm.kafka.demo.delivery

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@EmbeddedKafka(topics = ["delivery"])
class DeliveryEventHandlerTest {

    @Autowired
    lateinit var deliveryRepository: DeliveryRepository

    @Test
    fun givenDeliveryWhenPersistedThenPublishEvent() {
        val delivery = Delivery(item = "helloWorld", targetAddress = "berlin")

        deliveryRepository.save(delivery)

        // test if message was produced
    }
}