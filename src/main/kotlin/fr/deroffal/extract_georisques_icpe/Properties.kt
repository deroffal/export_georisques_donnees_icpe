package fr.deroffal.extract_georisques_icpe

import fr.deroffal.extract_georisques_icpe.data.adapters.Database
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/**
 * https://spring.io/guides/tutorials/spring-boot-kotlin/
 */
@ConstructorBinding
@ConfigurationProperties("app")
data class AppProperties(val rest: Rest, val db: Db) {
    data class Rest(val baseUrl: String)
    data class Db(val name: Database)
}