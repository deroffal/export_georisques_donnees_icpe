package fr.deroffal.extract_georisques_icpe.batch.rest

import fr.deroffal.extract_georisques_icpe.batch.rest.EtatActivite.EN_CONSTRUCTION
import fr.deroffal.extract_georisques_icpe.batch.rest.EtatActivite.EN_FONCTIONNEMENT
import fr.deroffal.extract_georisques_icpe.data.Localisation
import fr.deroffal.extract_georisques_icpe.data.Situation
import fr.deroffal.extract_georisques_icpe.data.Texte
import java.time.LocalDate


//https://www.georisques.gouv.fr/webappReport/ws/installations/etablissement/0063-03511/situation
data class SituationDto(
    val seveso: String,
    val codeNomenclature: String?,
    val alinea: String?,
    val dateAutorisation: LocalDate?,
    val etatActivite: Int,
    val regime: String?,
    val idRegime: String?,
    val activiteNomenclatureInst: String?,
    val familleNomenclature: String?,
    val volumeInst: String?,
    val unite: String?
) {
    fun aAfficher() =
        etatActivite in listOf(EN_CONSTRUCTION, EN_FONCTIONNEMENT).map { it.code }

    fun toSituation() = Situation(
        seveso = seveso,
        codeNomenclature = codeNomenclature,
        alinea = alinea,
        dateAutorisation = dateAutorisation,
        etatActivite = EtatActivite.values().find { it.code == etatActivite }!!,
        regime = regime,
        idRegime = idRegime,
        activiteNomenclatureInst = activiteNomenclatureInst,
        familleNomenclature = familleNomenclature,
        volumeInst = volumeInst,
        unite = unite
    )
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
    val codeSiret: Long?,

    val regionInst: String?,
    val departementInst: String?,
    val communeInst: String?,
    val adresse1: String?,
    val adresse2: String?,
    val codePostal: String?,
    val codeInsee: String?,
    val x: Long?,
    val y: Long?,

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
) {
    fun toLocalisation() = Localisation(
        region = regionInst,
        departement = departementInst,
        commune = communeInst,
        adresse1 = adresse1,
        adresse2 = adresse2,
        codePostal = codePostal,
        codeInsee = codeInsee,
        x = x,
        y = y
    )
}


data class TexteDto(val dateDoc: LocalDate?, val typeDoc: String?, val descriptionDoc: String?, val urlDoc: String?) {
    fun isNotEmpty() = listOfNotNull(dateDoc, typeDoc, descriptionDoc, urlDoc).isNotEmpty()

    fun toTexte() = Texte(
        dateDoc = dateDoc,
        typeDoc = typeDoc,
        descriptionDoc = descriptionDoc,
        urlDoc = urlDoc
    )
}