package fr.deroffal.extract_georisques_icpe.batch.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

const val baseUrl = "http://www.georisques.gouv.fr/webappReport/ws/installations"

@Configuration
class RestConfiguration {
    @Bean
    fun mapper(): ObjectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())
}


@Service
class HttpBuilder {

    private val httpClient = HttpClient.newBuilder().build()

    private val getRequestBuilder = HttpRequest.newBuilder()
        .setHeader("User-Agent", "export_georisques_donnees_icpe")
        .GET()

    private val bodyHandler = HttpResponse.BodyHandlers.ofString()
    fun getAsString(uri: String): String {
        val httpRequest = getRequestBuilder.uri(URI.create(uri)).build()
        return httpClient.send(httpRequest, bodyHandler).body()
    }
}
