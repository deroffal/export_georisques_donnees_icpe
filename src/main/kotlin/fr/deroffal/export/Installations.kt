package fr.deroffal.export

import java.time.LocalDate

data class Texte(val dateDoc: LocalDate?, val typeDoc: String?, val descriptionDoc: String?, val urlDoc: String?) {
    fun isNotEmpty() = listOfNotNull(dateDoc, typeDoc, descriptionDoc, urlDoc).isNotEmpty()
}

data class EtablissementCsv(
    val numeroInspection: String,
    val nom: String,
    val codePostal: String,
    val commune: String,
    val departement: String,
    val regimeEnVigueur: String,
    val statutSeveso: String,
    val etatActivite: String,
    val prioriteNationale: String,
    val iedMtd: String
) {
    fun getNumeroEtablissement() = numeroInspection.replace('.', '-')
}

data class Etablissement(
    val idInst: String,
    val nomInst: String,
    val codeSiret: String?,
    val regionInst: String,
    val departementInst: String,
    val communeInst: String,
    val adresse1: String?,
    val adresse2: String?,
    val codePostal: String,
    val codeInsee: String,
    val x: String,
    val y: String,
    val systProj: String,
    val codeActiviteInst: String?,
    val activiteInst: String?,
    val etatActiviteInst: String?,
    val nomServiceInspection: String,
    val numInspection: String,
    val derInspection: String?,
    val regimeInst: String,
    val prioNational: String,
    val pied: String,
    val statutInst: String
)