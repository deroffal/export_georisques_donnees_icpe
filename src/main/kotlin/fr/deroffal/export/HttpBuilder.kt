package fr.deroffal.export

import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

class HttpBuilder {

    private val httpClient = HttpClient.newBuilder().build()

    private val getRequestBuilder = HttpRequest.newBuilder()
        .setHeader("User-Agent", "export_georisques_donnees_icpe")
        .GET()

    fun getAsString(uri: String) =
        httpClient.send(getRequestBuilder.uri(URI.create(uri)).build(), HttpResponse.BodyHandlers.ofString()).body()
}