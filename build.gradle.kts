import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.3.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("groovy")
    id("maven-publish")
    kotlin("jvm") version "1.3.72"
    kotlin("plugin.spring") version "1.3.72"
    kotlin("plugin.jpa") version "1.3.72"
    kotlin("plugin.allopen") version "1.3.72"
    kotlin("kapt") version "1.3.72"
}

group = project.group
//version = project.version
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
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

//    implementation(enforcedPlatform("com.fasterxml.jackson:jackson-bom:2.11.3"))
    implementation("com.fasterxml.jackson.core:jackson-databind:2.11.3")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.11.3")
    implementation("com.fasterxml.jackson.module:jackson-module-parameter-names:2.11.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8:2.11.3")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.11.3")

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

//https://github.com/actions/setup-java#publishing-using-gradle
//https://github.com/deroffal/extract_georisques_icpe/new/master?filename=.github%2Fworkflows%2Fgradle-publish.yml&workflow_template=gradle-publish
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            artifactId = "extract_georisques_icpe"


            pom {
                name.set("extract_georisques_icpe")
                description.set("Export de données ICPE depuis georisques.gouv.fr ")
                url.set("https://github.com/deroffal/extract_georisques_icpe")
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/deroffal/extract_georisques_icpe/blob/master/LICENSE")
                    }
                }
                developers {
                    developer {
                        id.set("deroffal")
                    }
                }
                scm {
                    url.set("https://github.com/deroffal/extract_georisques_icpe")
                }
            }
        }

        println("GITHUB_ACTOR : ${System.getenv("GITHUB_ACTOR")}")
        println("GITHUB_TOKEN : ${System.getenv("GITHUB_TOKEN")}")
        println("GITHUB_TOKEN length : ${System.getenv("GITHUB_TOKEN")?.length ?: "vide"}")

        repositories {
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/deroffal/extract_georisques_icpe")
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}

