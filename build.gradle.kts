plugins {
	java
	id("org.springframework.boot") version "3.4.0"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("jvm") version "2.1.0"
	kotlin("plugin.spring") version "2.1.0"
	jacoco
}

group = "com.parkingapp"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
	mavenCentral()
}

dependencies {
	val REST_ASSURED = "5.5.0"
	val RETROFIT = "2.11.0"

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")
	implementation("org.postgresql:postgresql:42.7.4")
	implementation("org.flywaydb:flyway-core:9.11.0")
	implementation("org.springdoc:springdoc-openapi-ui:1.8.0")
	implementation("jakarta.mail:jakarta.mail-api:2.1.3")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.thymeleaf:thymeleaf:3.1.2.RELEASE")
	implementation("com.squareup.retrofit2:retrofit:$RETROFIT")
	implementation("com.squareup.retrofit2:converter-jackson:$RETROFIT")

	// Lombok
	compileOnly("org.projectlombok:lombok:1.18.36")
	annotationProcessor("org.projectlombok:lombok:1.18.36")

	// Lombok
	testCompileOnly("org.projectlombok:lombok:1.18.36")
	testAnnotationProcessor("org.projectlombok:lombok:1.18.36")


	// Test dependencies
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation(platform("org.junit:junit-bom:5.11.3"))
	testImplementation("org.junit.jupiter:junit-jupiter")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.assertj:assertj-core:3.26.3")
	testImplementation("org.mockito:mockito-core:5.+")

	testImplementation(platform("org.testcontainers:testcontainers-bom:1.20.4"))
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("io.rest-assured:rest-assured:$REST_ASSURED")
	testImplementation("io.rest-assured:json-path:$REST_ASSURED")
	testImplementation("io.rest-assured:xml-path:$REST_ASSURED")
	testImplementation("io.rest-assured:spring-mock-mvc:$REST_ASSURED")
	testImplementation("io.rest-assured:spring-commons:$REST_ASSURED")
	testImplementation("org.wiremock:wiremock-standalone:3.10.0")
	testImplementation("org.awaitility:awaitility:4.2.2")
	testImplementation("com.tngtech.archunit:archunit:1.3.0")
	testImplementation("com.icegreen:greenmail:2.1.2")
}

jacoco {
	toolVersion = "0.8.12"
}

tasks.apply {
	test {
		enableAssertions = true
		useJUnitPlatform {
			excludeTags("integration")
			excludeTags("component")
			excludeTags("contract")
		}
		finalizedBy(jacocoTestReport)
	}

	task<Test>("integrationTest") {
		group = "verification"
		description = "Runs integration tests."
		useJUnitPlatform {
			includeTags("integration")
		}
		shouldRunAfter(test)
	}

	task<Test>("contractTest") {
		group = "verification"
		description = "Runs contract tests."
		useJUnitPlatform {
			includeTags("contract")
		}
		shouldRunAfter("integrationTest")
	}

	task<Test>("componentTest") {
		group = "verification"
		description = "Runs component tests."
		useJUnitPlatform {
			includeTags("component")
		}
		shouldRunAfter("contractTest")
	}

	check {
		dependsOn("integrationTest", "contractTest", "componentTest")
	}

	jacocoTestReport {
		val jacocoDir = layout.buildDirectory.dir("jacoco")
		executionData(
			fileTree(jacocoDir).include(
				"/test.exec",
				"/contractTest.exec",
				"/integrationTest.exec"
			)
		)
		reports {
			csv.required.set(false)
			html.required.set(true)
			xml.required.set(true)
			html.outputLocation.set(layout.buildDirectory.dir("jacoco/html"))
			xml.outputLocation.set(layout.buildDirectory.file("jacoco/report.xml"))
		}
		dependsOn(test, "integrationTest", "contractTest")
	}

	jacocoTestCoverageVerification {
		val jacocoDir = layout.buildDirectory.dir("jacoco")
		executionData(
			fileTree(jacocoDir).include(
				"test.exec",
				"contractTest.exec",
				"integrationTest.exec"
			)
		)
		violationRules {
			rule {
				limit {
					minimum = "0.80".toBigDecimal()
				}
			}
		}
		dependsOn(test, "integrationTest", "contractTest")
	}
}
