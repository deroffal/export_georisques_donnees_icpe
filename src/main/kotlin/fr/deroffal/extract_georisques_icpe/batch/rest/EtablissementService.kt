package fr.deroffal.extract_georisques_icpe.batch.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.deroffal.extract_georisques_icpe.data.Etablissement
import org.springframework.stereotype.Service

@Service
class EtablissementService(
    private val httpBuilder: HttpBuilder,
    private val mapper: ObjectMapper
) {

    fun listerEtablissements(parametreExport: ParametreExport): List<Etablissement> {
        return httpBuilder.getAsString(parametreExport.buildUri())
            .split("\r\n").filterNot { it.isEmpty() }.drop(1)
            .map { it.split(";") }
            .map {
                Etablissement(
                    idInst = it[0],
                    nom = it[1],
                    codePostal = it[2],
                    commune = it[3],
                    departement = it[4],
                    regimeEnVigeur = it[5],
                    statutSeveso = it[6],
                    etatActivite = it[7],
                    prioriteNationale = it[8]
//                    iedMtd = it[9]
                )
            }
    }

    fun recupererEtablissement(numeroEtablissement: String): EtablissementDto {
        val etablissementStr = httpBuilder.getAsString("$baseUrl/etablissement/$numeroEtablissement")
        return mapper.readValue(etablissementStr)
    }
}


//data class EtablissementCsv(
//    val numeroInspection: String,
//    val nom: String,
//    val codePostal: String,
//    val commune: String,
//    val departement: String,
//    val regimeEnVigueur: String,
//    val statutSeveso: String,
//    val etatActivite: String,
//    val prioriteNationale: String,
//    val iedMtd: String
//) {
//    fun getNumeroEtablissement() = numeroInspection.replace('.', '-')
//}
