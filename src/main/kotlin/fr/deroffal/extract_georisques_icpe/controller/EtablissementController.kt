package fr.deroffal.extract_georisques_icpe.controller

import fr.deroffal.extract_georisques_icpe.data.Etablissement
import fr.deroffal.extract_georisques_icpe.data.EtablissementRepository
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class EtablissementController(
    private val etablissementRepository: EtablissementRepository
) {

    @GetMapping("/etablissement/list")
    @ResponseStatus(OK)
    fun etablissements(): MutableIterable<Etablissement> {
        return etablissementRepository.findAll()
    }
}
