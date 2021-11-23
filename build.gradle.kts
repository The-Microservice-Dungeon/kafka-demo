import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.5.6"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	id("org.jetbrains.kotlin.plugin.noarg") version "1.3.41"
	kotlin("jvm") version "1.5.31"
	kotlin("plugin.jpa") version "1.4.32"
	kotlin("plugin.spring") version "1.5.31"
	kotlin("plugin.allopen") version "1.4.32"
}

noArg {
	annotation("de.msdungeon.demo.player.core.util.NoArg")
}

allOpen {
	annotation("javax.persistence.Entity")
	annotation("javax.persistence.Embeddable")
	annotation("javax.persistence.MappedSuperclass")
}

group = "de.pschm"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven("https://jitpack.io")
}

val springBootVersion = "2.5.6"
val kafkaVersion = "2.7.9"


dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
	implementation("javax.validation:validation-api:2.0.1.Final")

	implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:$springBootVersion")
	implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
	implementation("org.springframework.kafka:spring-kafka:$kafkaVersion")

	implementation("io.github.microutils:kotlin-logging-jvm:2.0.11")
	implementation("com.github.javafaker:javafaker:1.0.2")


	annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor:$springBootVersion")
	developmentOnly("org.springframework.boot:spring-boot-devtools:$springBootVersion")
	runtimeOnly("com.h2database:h2:1.4.200")

	testImplementation("io.mockk:mockk:1.12.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
	testImplementation("org.springframework.kafka:spring-kafka-test:$kafkaVersion")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
