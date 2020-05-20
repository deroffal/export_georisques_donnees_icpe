package fr.deroffal.export

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.FileWriter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

const val paramPaysDeLaLoire = "region=52"
const val paramLoireAtlantique = "departement=44"
const val paramNantes = "commune=44109"
const val baseUrl = "http://www.georisques.gouv.fr/webappReport/ws/installations"

val httpBuilder = HttpBuilder()
val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())

fun main(args: Array<String>) {

    println("${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)} - Début")

    val now = LocalDate.now().format(DateTimeFormatter.ISO_DATE)

    val etablissements =
        httpBuilder.getAsString("$baseUrl/sitesdetails/detailsites_$now.csv?etablissement=&isExport=true")
//        httpBuilder.getAsString("$baseUrl/sitesdetails/detailsites_$now.csv?etablissement=&$paramNantes&isExport=true")
            .split("\r\n").filterNot { it.isEmpty() }.drop(1)
            .map { it.split(";") }
            .map {
                EtablissementCsv(
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

    println("${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)} - ${etablissements.size} établissements trouvés...")

    val lignesCSV = etablissements.map {
        val textes = recupererTextes(it)
        val etablissement = recupererEtablissement(it)

        listOf(
            it.numeroInspection,
            it.nom,
            it.codePostal,
            it.commune,
            it.departement,
            it.regimeEnVigueur,
            it.statutSeveso,
            it.etatActivite,
            it.prioriteNationale,
            it.iedMtd,
            etablissement.activiteInst,
            etablissement.derInspection
        ) + textes.map {
            listOf(
                it.dateDoc?.format(DateTimeFormatter.ISO_DATE),
                it.typeDoc,
                it.descriptionDoc,
                it.urlDoc
            )
        }.flatten()
    }
    val fileWriter = FileWriter("export_${Date().time}.csv")


    val entete =
        "Numéro d'inspection;Nom établissement;Code postal;Commune;Département;Régime en vigueur;Statut SEVESO;Etat d’activité;Priorité nationale;IED-MTD;Activité;Dernière inspection" + (1..24).map { ";Date document;Type document;Description document;URL document" }
            .joinToString(postfix = "\n")
    fileWriter.write(entete)
    lignesCSV
        .map { it.joinToString(separator = ";", postfix = "\n") { it?.replace(';', ',') ?: "" } }
        .forEach {
            fileWriter.write(it)
        }

    println("${LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)} - Fin")
}

private fun recupererTextes(etablissementCsv: EtablissementCsv): List<Texte> {
    val texteStr = httpBuilder.getAsString("$baseUrl/etablissement/${etablissementCsv.getNumeroEtablissement()}/texte")
    return mapper
        .readValue<Collection<Texte>>(texteStr)
        .filter { it.isNotEmpty() }
        .sortedBy { it.dateDoc }.reversed()
}

private fun recupererEtablissement(etablissementCsv: EtablissementCsv): Etablissement {
    val etablissementStr = httpBuilder.getAsString("$baseUrl/etablissement/${etablissementCsv.getNumeroEtablissement()}")
    return mapper.readValue(etablissementStr)
}