package fr.deroffal.extract_georisques_icpe.batch.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.deroffal.extract_georisques_icpe.AppProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Configuration
class RestConfiguration {
    @Bean
    fun mapper(): ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
}

interface RestConfig {
    fun getBaseUrl(): String
}

@Service
@Profile("!test")
class PropertiesRestConfig(private val properties: AppProperties) : RestConfig {
    override fun getBaseUrl() = properties.rest.baseUrl
}

@Service
@Profile("test")
class SystemPropertiesConfig(private val properties: AppProperties) : RestConfig {
    override fun getBaseUrl() = System.getProperty("app.rest.baseUrl")
}