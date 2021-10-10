import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.2"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.4.21"
	kotlin("plugin.spring") version "1.4.21"
	id("org.jetbrains.kotlin.plugin.jpa") version "1.4.21"
}

group = "com.bassmeister"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {

	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.kafka:spring-kafka")
	//TODO: Check If I still need this one
	implementation("org.springframework.boot:spring-boot-starter-data-rest"){
		exclude("org.springframework.boot","spring-boot-starter-web")
	}
	implementation("org.springframework.boot:spring-boot-starter-integration")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.integration:spring-integration-mail:5.4.3")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:2.5.5")
	implementation("javax.mail:javax.mail-api:1.6.2")
	implementation("com.sun.mail:javax.mail:1.6.2")
	implementation("junit:junit:4.12")
	runtimeOnly("com.h2database:h2")
	runtimeOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation ("org.junit.jupiter:junit-jupiter:5.6.0")
	testImplementation("org.junit.platform:junit-platform-runner:1.5.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.springframework.boot:spring-boot-starter-mail")
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict", "-Xemit-jvm-type-annotations")
		jvmTarget = "1.8"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}


