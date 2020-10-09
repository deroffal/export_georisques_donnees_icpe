package fr.deroffal.extract_georisques_icpe.batch.rest.services

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.deroffal.extract_georisques_icpe.batch.rest.HttpBuilder
import fr.deroffal.extract_georisques_icpe.batch.rest.beans.TexteDto
import org.springframework.stereotype.Service

@Service
class TexteService(
    private val httpBuilder: HttpBuilder,
    private val mapper: ObjectMapper
) {

    //;Date document;Type document;Description document;URL document
    fun recupererTextes(numeroEtablissement: String): List<TexteDto> {
        val texteStr = httpBuilder.getAsString("/etablissement/${numeroEtablissement}/texte")
        return mapper
            .readValue<Collection<TexteDto>>(texteStr)
            .filter { it.isNotEmpty() }
            .sortedBy { it.dateDoc }.reversed()
    }
}