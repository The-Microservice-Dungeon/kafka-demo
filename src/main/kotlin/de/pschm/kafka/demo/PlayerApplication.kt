package de.pschm.kafka.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
@EnableKafka
class PlayerApplication

fun main(args: Array<String>) {
    runApplication<PlayerApplication>(*args)
}