package fr.deroffal.export

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.deroffal.export.service.*
import fr.deroffal.export.util.HttpBuilder
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*

const val baseUrl = "http://www.georisques.gouv.fr/webappReport/ws/installations"

val httpBuilder = HttpBuilder()
val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())

fun main() {
    println("${LocalDateTime.now().format(ISO_DATE_TIME)} - Début")


    val params = ParametreExport.Builder()
        .parametreGeographiqueExport(ParametreGeographiqueExport.NANTES)
        .exclureSeveso(true)
        .build()

    val etablissements: List<EtablissementCsv> = listerEtablissements(params)

    println("${LocalDateTime.now().format(ISO_DATE_TIME)} - ${etablissements.size} établissements trouvés...")


    var maxSituationSize = 0
    val lignesCSV = etablissements.map {
        val numeroEtablissement = it.getNumeroEtablissement()
        val etablissement = recupererEtablissement(numeroEtablissement)
        val situation = recupererSituations(numeroEtablissement)

        maxSituationSize = maxOf(maxSituationSize, situation.size)

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
            it.iedMtd
        ) + listOf(
            etablissement.activiteInst,
            etablissement.derInspection
        ) + situation.map { s ->
            listOf(
                s.codeNomenclature,
                s.regime
            )
        }.flatten()
    }
    val fileWriter = FileWriter("export_${LocalDateTime.now().format(ofPattern("yyyyMMddHHmmss"))}.csv")

    fileWriter.write(
        "Numéro d'inspection;Nom établissement;Code postal;Commune;Département;Régime en vigueur;Statut SEVESO;Etat d’activité;Priorité nationale;IED-MTD;Activité;Dernière inspection" +
                (1..maxSituationSize).joinToString(postfix = "\n") { ";Rubrique IC;Régime autorisé" }
//                + (1..24).joinToString(postfix = "\n") { ";Date document;Type document;Description document;URL document" }
    )
    lignesCSV
        .map { it.joinToString(separator = ";", postfix = "\n") { it?.replace(';', ',') ?: "" } }
        .forEach {
            fileWriter.write(it)
        }

    println("${LocalDateTime.now().format(ISO_DATE_TIME)} - Fin")
}



