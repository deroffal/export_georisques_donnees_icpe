import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("groovy")
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
    kotlin("plugin.allopen") version "1.3.72"
    kotlin("kapt") version "1.3.72"
}

group = "fr.deroffal"
version = "0.2-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    implementation(enforcedPlatform("com.fasterxml.jackson:jackson-bom:2.11.3"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    kapt("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.mapstruct:mapstruct:1.4.0.CR1")
    kapt("org.mapstruct:mapstruct-processor:1.4.0.CR1")

    implementation("org.liquibase:liquibase-core:4.0.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("org.springframework.batch:spring-batch-test")

    testImplementation("org.codehaus.groovy:groovy-all:2.5.13")
    testImplementation("org.spockframework:spock-core:2.0-M3-groovy-2.5")
    testImplementation("org.spockframework:spock-spring:2.0-M3-groovy-2.5")

    testImplementation("org.testcontainers:jdbc:1.14.3")
    testImplementation("org.testcontainers:postgresql:1.14.3")
    testImplementation("org.testcontainers:spock:1.14.3")

    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-stub-runner:2.2.4.RELEASE")

    testImplementation("org.dbunit:dbunit:2.7.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("spring.profiles.active", "test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

tasks.test {
    testLogging {
        events("passed", "skipped", "failed")
        showStandardStreams = true
        showStackTraces = true
        showCauses = true
        exceptionFormat = FULL
    }
    ignoreFailures = System.getProperties().getProperty("test.ignoreFailures")?.toBoolean() ?: false
    systemProperty("spring.profiles.active", "test")
}