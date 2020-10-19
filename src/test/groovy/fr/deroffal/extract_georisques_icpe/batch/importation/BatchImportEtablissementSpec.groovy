package fr.deroffal.extract_georisques_icpe.batch.importation

import fr.deroffal.extract_georisques_icpe.batch.AbstractBatchSpec
import fr.deroffal.extract_georisques_icpe.data.EtablissementRepository
import fr.deroffal.extract_georisques_icpe.util.WithWireMockSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Subject
import spock.lang.Title

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@Title("Vérification du batch d'importation")
@Subject(BatchImportEtablissementConfiguration)
@SpringBootTest(webEnvironment = RANDOM_PORT, properties = ["app.rest.baseUrl=http://localhost:6443"])
class BatchImportEtablissementSpec extends AbstractBatchSpec implements WithWireMockSpecification {

    @Autowired
    EtablissementRepository etablissementRepository

    void "batch_importation"() {
        setup:
        dateService.setTime('2020-10-19T18:35:24.00Z')

        when:
        HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(batchURI).GET().build(),
                HttpResponse.BodyHandlers.ofString()
        )

        waitForJobExecutionToFinish()
        then:
        verifyJobStatusFinished()

        cleanup:
        dateService.resetTime()
    }

    private URI getBatchURI() {
        return "$baseUrl/etablissement/launch".toURI()
    }
}
