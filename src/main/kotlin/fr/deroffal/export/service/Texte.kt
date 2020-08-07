package fr.deroffal.export.service

import com.fasterxml.jackson.module.kotlin.readValue
import fr.deroffal.export.baseUrl
import fr.deroffal.export.httpBuilder
import fr.deroffal.export.mapper
import java.time.LocalDate

data class Texte(val dateDoc: LocalDate?, val typeDoc: String?, val descriptionDoc: String?, val urlDoc: String?) {
    fun isNotEmpty() = listOfNotNull(dateDoc, typeDoc, descriptionDoc, urlDoc).isNotEmpty()
}

//;Date document;Type document;Description document;URL document
private fun recupererTextes(etablissementCsv: EtablissementCsv): List<Texte> {
    val texteStr = httpBuilder.getAsString("$baseUrl/etablissement/${etablissementCsv.getNumeroEtablissement()}/texte")
    return mapper
        .readValue<Collection<Texte>>(texteStr)
        .filter { it.isNotEmpty() }
        .sortedBy { it.dateDoc }.reversed()
}
