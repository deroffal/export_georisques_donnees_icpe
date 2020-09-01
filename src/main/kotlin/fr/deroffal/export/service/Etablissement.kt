package fr.deroffal.export.service

import com.fasterxml.jackson.module.kotlin.readValue


//fun recupererEtablissement(numeroEtablissement: String): Etablissement {
//    val etablissementStr = httpBuilder.getAsString("$baseUrl/etablissement/$numeroEtablissement")
//    return mapper.readValue(etablissementStr)
//}

data class Etablissement(
    val idInst: String?,
    val nomInst: String?,
    val codeSiret: String?,
    val regionInst: String?,
    val departementInst: String?,
    val communeInst: String?,
    val adresse1: String?,
    val adresse2: String?,
    val codePostal: String?,
    val codeInsee: String?,
    val x: String?,
    val y: String?,
    val systProj: String?,
    val codeActiviteInst: String?,
    val activiteInst: String?,
    val etatActiviteInst: String?,
    val nomServiceInspection: String?,
    val numInspection: String?,
    val derInspection: String?,
    val regimeInst: String?,
    val prioNational: String?,
    val pied: String?,
    val statutInst: String?
)
