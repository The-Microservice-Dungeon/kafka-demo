package de.pschm.kafka.demo.customer

import com.github.javafaker.Faker
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Customer(
    @Id val id: String,
    val name: String,
    val jobTitle: String,
    var address: String,
    val favoriteColor: String = Faker.instance().color().name(),
    val favoriteSomething: String = Faker.instance().hitchhikersGuideToTheGalaxy().quote()
) {
    fun toDto() = CustomerDto(id, name, jobTitle, address)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Customer

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , jobTitle = $jobTitle , favoriteColor = $favoriteColor , favoriteSomething = $favoriteSomething , address = $address )"
    }
}