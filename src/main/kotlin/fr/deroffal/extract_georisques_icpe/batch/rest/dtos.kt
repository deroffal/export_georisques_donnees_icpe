package fr.deroffal.extract_georisques_icpe.batch.rest

import java.time.LocalDate

//https://www.georisques.gouv.fr/webappReport/ws/installations/etablissement/0063-03511/situation
data class Situation(
    val seveso: String,
    val codeNomenclature: String?,//
    val alinea: String?,
    val dateAutorisation: LocalDate?,//
    val etatActivite: Int,
    val regime: String?,
    val idRegime: String?,
    val activiteNomenclatureInst: String?,
    val familleNomenclature: String?,
    val volumeInst: String?,
    val unite: String?
) {
    fun aAfficher() =
        etatActivite in listOf(EtatActivite.EN_CONSTRUCTION, EtatActivite.EN_FONCTIONNEMENT).map { it.code }
}


//https://www.georisques.gouv.fr/sites/all/modules/custom/dossier_installations/js/detailsInstallations.js?qelarj
enum class EtatActivite(val code: Int) {
    A_L_ARRET(0),
    EN_FONCTIONNEMENT(1),
    EN_CONSTRUCTION(2)
}

data class EtablissementDto(
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

data class Texte(val dateDoc: LocalDate?, val typeDoc: String?, val descriptionDoc: String?, val urlDoc: String?) {
    fun isNotEmpty() = listOfNotNull(dateDoc, typeDoc, descriptionDoc, urlDoc).isNotEmpty()
}