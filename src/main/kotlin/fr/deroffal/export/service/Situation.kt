package fr.deroffal.export.service

import com.fasterxml.jackson.module.kotlin.readValue
import fr.deroffal.export.service.EtatActivite.EN_CONSTRUCTION
import fr.deroffal.export.service.EtatActivite.EN_FONCTIONNEMENT
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
    fun aAfficher() = etatActivite in listOf(EN_CONSTRUCTION, EN_FONCTIONNEMENT).map { it.code }
}

//fun recupererSituations(numeroEtablissement: String): List<Situation> {
//    val etablissementStr = httpBuilder.getAsString("$baseUrl/etablissement/$numeroEtablissement/situation")
//    return mapper.readValue<Collection<Situation>>(etablissementStr)
//        .filter { it.aAfficher() }
//        .sortedWith(SituationComparator())
//}


//https://www.georisques.gouv.fr/sites/all/modules/custom/dossier_installations/js/detailsInstallations.js?qelarj
enum class EtatActivite(val code: Int) {
    A_L_ARRET(0),
    EN_FONCTIONNEMENT(1),
    EN_CONSTRUCTION(2)
}

/**
 * autorisation > enregistrement > tous le reste
 */
private class SituationComparator : Comparator<Situation> {
    override fun compare(o1: Situation?, o2: Situation?): Int {
        if (o1 == null && o2 == null) {
            return 1
        }
        if (o1 == null) {
            return -1
        }
        if (o2 == null) {
            return 1
        }
        return (o1.regime ?: "z").compareTo(o2.regime ?: "z")
    }

}
