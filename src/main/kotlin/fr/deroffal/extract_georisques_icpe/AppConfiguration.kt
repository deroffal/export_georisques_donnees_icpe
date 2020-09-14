package fr.deroffal.extract_georisques_icpe

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * https://spring.io/guides/tutorials/spring-boot-kotlin/
 */
@ConstructorBinding
@ConfigurationProperties("app")
data class AppProperties(val rest: Rest) {
    data class Rest(val baseUrl: String)
}