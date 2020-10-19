package fr.deroffal.extract_georisques_icpe.batch.rest.services

import fr.deroffal.extract_georisques_icpe.util.WithWireMockSpecification
import fr.deroffal.extract_georisques_icpe.batch.rest.beans.TexteDto
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class TexteServiceSpec extends Specification implements WithWireMockSpecification {

    @Autowired
    TexteService texteService

    /**
     * http://www.georisques.gouv.fr/webappReport/ws/installations/etablissement/0063-03511/texte
     */
    void "Recuperation des textes d'un etablissement"() {
        given:
        String numeroEtablissement = '0063-03511'
        when:
        List<TexteDto> textes = texteService.recupererTextes(numeroEtablissement).sort { it.dateDoc }
        then:
        textes*.dateDoc*.toString() == [
                '2000-05-31',
                '2003-02-20',
                '2016-05-10',
                '2020-08-03'
        ]
        textes*.typeDoc == [
                "Arrêté préfectoral",
                "Arrêté préfectoral",
                "Arrêté de mise en demeure",
                "Arrêté préfectoral"
        ]
        textes*.descriptionDoc == [
                "AP AUTO 2000",
                "APC 2003",
                "APMD 2016",
                "AP 28-07/20 abrogeant APMD 10-05-2016",

        ]
        textes*.urlDoc == [
                "N/c/8aac032456307ed70156319190f7000c.pdf",
                "N/5/8aac03245659950801565a35791e0005.pdf",
                "N/7/8aac032456927819015692b9725a0057.pdf",
                "N/9/8aac032473b341db0173b45176fb0019.pdf"
        ]
    }

}
