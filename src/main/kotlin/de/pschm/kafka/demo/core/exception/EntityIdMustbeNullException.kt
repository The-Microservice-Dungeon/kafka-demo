package de.pschm.kafka.demo.core.exception

class EntityIdMustBeNullException(entity: String?) :
    RuntimeException(String.format("A newly created '%s' cannot have an id.", entity))