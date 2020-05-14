package fr.deroffal.export

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun main(args: Array<String>) {

    val httpClient = HttpClient.newBuilder().build()

    val now = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
//    val loireAtlantique = 52
    val nantes = 44109

    val requestBuilder =
        HttpRequest.newBuilder()
//        HttpRequest.newBuilder(URI.create("http://www.georisques.gouv.fr/webappReport/ws/installations/sitesdetails/detailsites_${now}.csv?etablissement=&region=${loireAtlantique}&isExport=true"))
            .GET()
            .setHeader("User-Agent", "export_georisques_donnees_icpe")

    val httpResponse = httpClient.send(requestBuilder.uri(URI.create("http://www.georisques.gouv.fr/webappReport/ws/installations/sitesdetails/detailsites_${now}.csv?etablissement=&commune=${nantes}&isExport=true")).build(), HttpResponse.BodyHandlers.ofString())

    val body = httpResponse.body().split("\r\n").filterNot { it.isEmpty() }.drop(1)
        .map { it.split(";") }
        .map {
            Site(
                numeroInspection = it[0],
                nom = it[1],
                codePostal = it[2],
                commune = it[3],
                departement = it[4],
                regimeEnVigueur = it[5],
                statutSeveso = it[6],
                etatActivite = it[7],
                prioriteNationale = it[8],
                iedMtd = it[9]
            )
        }


    val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())

    body.onEach {
        val numeroInspection = it.numeroInspection.replace('.', '-')
        val textesRequest =
            requestBuilder.uri(URI("http://www.georisques.gouv.fr/webappReport/ws/installations/etablissement/$numeroInspection/texte"))
                .build()
        val textesResponse = httpClient.send(textesRequest, HttpResponse.BodyHandlers.ofString())
        val content = textesResponse.body()
        val textes = mapper.readValue<Collection<Texte>>(content).sortedBy { it.dateDoc }
        it.textes.addAll(textes)
    }.filter { it.textes.isNotEmpty() }
        .forEach {
            println(it.nom)
        }
    println(body)
}
