package fr.deroffal.export.service

import fr.deroffal.export.baseUrl
import fr.deroffal.export.httpBuilder
import java.time.LocalDate
import java.time.format.DateTimeFormatter


fun listerEtablissements(parametreExport: ParametreExport): List<EtablissementCsv> {
    return httpBuilder.getAsString(parametreExport.buildUri())
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
}

class ParametreExport private constructor(
    val parametreGeographiqueExport: ParametreGeographiqueExport?,
    val exclureSeveso: Boolean = false
) {

    private fun hasParametreGeographique() = parametreGeographiqueExport != null
    private fun getParametreGeographique() = if (hasParametreGeographique()) parametreGeographiqueExport!!.asUrlParam() else ""

    fun buildUri(): String {
        //URI minimale pour avoir un export
        var baseUri = "$baseUrl/sitesdetails/detailsites_${LocalDate.now().format(DateTimeFormatter.ISO_DATE)}.csv?etablissement=&isExport=true"
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
        var exclureSeveso: Boolean = false
    ) {

        fun parametreGeographiqueExport(parametreGeographiqueExport: ParametreGeographiqueExport) = apply { this.parametreGeographiqueExport = parametreGeographiqueExport }
        fun exclureSeveso(exclureSeveso: Boolean) = apply { this.exclureSeveso = exclureSeveso }
        fun build() = ParametreExport(parametreGeographiqueExport, exclureSeveso)
    }
}

enum class ParametreGeographiqueExport(val clef: String, val code: Int) {

    NANTES("commune", 44109),
    LOIRE_ATLANTIQUE("departement", 44),
    PAYS_DE_LA_LOIRE("region", 52);

    fun asUrlParam() = "${this.clef}=${this.code}"
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
