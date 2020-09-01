package fr.deroffal.extract_georisques_icpe.controller

import fr.deroffal.extract_georisques_icpe.data.EtablissementRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class EtablissementController(
        private val etablissementRepository: EtablissementRepository
) {

    @GetMapping("/etablissement/list")
    fun etablissements() = etablissementRepository.findAll()
}
