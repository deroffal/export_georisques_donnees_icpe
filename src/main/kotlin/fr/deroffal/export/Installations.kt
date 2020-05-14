package fr.deroffal.export

import java.time.LocalDate

data class Texte(val dateDoc: LocalDate?, val typeDoc: String?, val descriptionDoc: String?, val urlDoc: String?)
data class Site(
    val numeroInspection: String,
    val nom: String,
    val codePostal: String,
    val commune: String,
    val departement: String,
    val regimeEnVigueur: String,
    val statutSeveso: String,
    val etatActivite: String,
    val prioriteNationale: String,
    val iedMtd: String,
    val textes: MutableList<Texte> = mutableListOf()
)