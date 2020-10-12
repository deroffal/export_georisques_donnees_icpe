package fr.deroffal.extract_georisques_icpe.batch.rest.services

import com.github.tomakehurst.wiremock.WireMockServer
import fr.deroffal.extract_georisques_icpe.batch.rest.beans.TexteDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.WireMockSpring
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification

////https://cloud.spring.io/spring-cloud-contract/reference/html/project-features.html#features-wiremock
@SpringBootTest("app.rest.baseUrl=http://localhost:6443")
@ContextConfiguration
class TexteServiceSpec extends Specification {

    @Shared
    WireMockServer wiremock = new WireMockServer(
            WireMockSpring.options().port(6443)
    )

    void setupSpec() {
        wiremock.start()
    }

    /**
     *  TODO The registered WireMock server is reset after each test class, however, if you need to reset it after each test method, just set the wiremock.reset-mappings-after-each-test property to true.
     */
    void cleanup() {
        wiremock.resetAll()
    }

    void cleanupSpec() {
        wiremock.shutdown()
    }

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
