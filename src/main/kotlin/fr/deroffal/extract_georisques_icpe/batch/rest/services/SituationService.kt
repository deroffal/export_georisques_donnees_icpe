package fr.deroffal.extract_georisques_icpe.batch.rest.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.deroffal.extract_georisques_icpe.batch.rest.HttpBuilder
import fr.deroffal.extract_georisques_icpe.batch.rest.beans.SituationDto
import org.springframework.stereotype.Service

@Service
class SituationService(
    private val httpBuilder: HttpBuilder,
    private val mapper: ObjectMapper
) {

    fun recupererSituations(numeroEtablissement: String): List<SituationDto> {
        val etablissementStr = httpBuilder.getAsString("/etablissement/$numeroEtablissement/situation")
        return mapper.readValue<Collection<SituationDto>>(etablissementStr)
            .filter { it.aAfficher() }
            .sortedWith(SituationComparator())
    }
}

/**
 * autorisation > enregistrement > tous le reste
 */
private class SituationComparator : Comparator<SituationDto> {
    override fun compare(o1: SituationDto?, o2: SituationDto?): Int {
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
