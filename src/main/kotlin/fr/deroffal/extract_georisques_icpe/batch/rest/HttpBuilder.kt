package fr.deroffal.extract_georisques_icpe.batch.rest

import org.springframework.stereotype.Service
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Service
class HttpBuilder(val restProperties: RestProperties) {

    private val httpClient = HttpClient.newBuilder().build()

    private val getRequestBuilder = HttpRequest.newBuilder()
        .setHeader("User-Agent", "export_georisques_donnees_icpe")
        .GET()

    private val bodyHandler = HttpResponse.BodyHandlers.ofString()

    fun getAsString(uri: String): String {
        val httpRequest = getRequestBuilder.uri(URI.create("${restProperties.getBaseUrl()}$uri")).build()
        return httpClient.send(httpRequest, bodyHandler).body()
    }
}

