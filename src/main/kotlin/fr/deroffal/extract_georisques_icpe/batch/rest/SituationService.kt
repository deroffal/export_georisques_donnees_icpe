package fr.deroffal.extract_georisques_icpe.batch.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
class SituationService(
    private val httpBuilder: HttpBuilder,
    private val mapper: ObjectMapper
) {

    fun recupererSituations(numeroEtablissement: String): List<Situation> {
        val etablissementStr = httpBuilder.getAsString("$baseUrl/etablissement/$numeroEtablissement/situation")
        return mapper.readValue<Collection<Situation>>(etablissementStr)
            .filter { it.aAfficher() }
            .sortedWith(SituationComparator())
    }
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

class ParametreExport private constructor(
    val parametreGeographiqueExport: ParametreGeographiqueExport?,
    val exclureSeveso: Boolean = false
) {

    private fun hasParametreGeographique() = parametreGeographiqueExport != null
    private fun getParametreGeographique() =
        if (hasParametreGeographique()) parametreGeographiqueExport!!.asUrlParam() else ""

    fun buildUri(): String {
        //URI minimale pour avoir un export
        var baseUri = "$baseUrl/sitesdetails/detailsites_${
            LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        }.csv?etablissement=&isExport=true"
        if (hasParametreGeographique()) {
            baseUri += "&${getParametreGeographique()}"
        }
        if (exclureSeveso) {
            baseUri += "&statut=NS"
        }
        return baseUri
    }

    data class Builder(
        var parametreGeographiqueExport: ParametreGeographiqueExport? = null,
        var exclureSeveso: Boolean = true
    ) {

        fun parametreGeographiqueExport(parametreGeographiqueExport: String?) = apply {
            this.parametreGeographiqueExport =
                if (parametreGeographiqueExport != null) ParametreGeographiqueExport.valueOf(parametreGeographiqueExport) else ParametreGeographiqueExport.NANTES
        }

        fun exclureSeveso(exclureSeveso: String?) = apply { this.exclureSeveso = (exclureSeveso ?: "true").toBoolean() }
        fun build() = ParametreExport(parametreGeographiqueExport, exclureSeveso)
    }
}

enum class ParametreGeographiqueExport(val clef: String, val code: Int) {

    NANTES("commune", 44109),
    LOIRE_ATLANTIQUE("departement", 44),
    PAYS_DE_LA_LOIRE("region", 52);

    fun asUrlParam() = "${this.clef}=${this.code}"
}