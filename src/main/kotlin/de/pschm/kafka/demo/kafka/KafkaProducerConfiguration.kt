package de.pschm.kafka.demo.kafka

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaProducerConfiguration(
    @Value("\${spring.kafka.bootstrap-servers}") val servers: String,
) {
    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> = KafkaTemplate(producerFactory())

    fun producerFactory(): ProducerFactory<String, String> = DefaultKafkaProducerFactory(producerConfig())

    fun producerConfig(): Map<String, Any> {
        val props = HashMap<String, Any>()

        props[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = servers
        props[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        props[ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG] = 500
        props[ProducerConfig.ACKS_CONFIG] = "all"
        props[ProducerConfig.RETRIES_CONFIG] = 2

        return props
    }
}