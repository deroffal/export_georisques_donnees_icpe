package fr.deroffal.extract_georisques_icpe.batch.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.deroffal.extract_georisques_icpe.AppProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service

@Configuration
class RestConfiguration {
    @Bean
    fun mapper(): ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
}

interface RestProperties {
    fun getBaseUrl(): String
}

@Service
class SimpleRestProperties(private val properties: AppProperties) : RestProperties {
    override fun getBaseUrl() = System.getProperty("app.rest.baseUrl") ?: properties.rest.baseUrl
}