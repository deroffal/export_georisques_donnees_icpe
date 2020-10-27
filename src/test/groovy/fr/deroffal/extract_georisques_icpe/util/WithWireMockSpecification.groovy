package fr.deroffal.extract_georisques_icpe.util

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.WireMockSpring
import spock.lang.Shared

////https://cloud.spring.io/spring-cloud-contract/reference/html/project-features.html#features-wiremock
@SpringBootTest("app.rest.baseUrl=http://localhost:6443")
trait WithWireMockSpecification {

    @Shared
    WireMockServer wiremock = new WireMockServer(
            WireMockSpring.options().port(6443)
    )

    void setupSpec() {
        wiremock.start()
    }

    void cleanup() {
        //The registered WireMock server is reset after each test class, however, if you need to reset it after each test method, just set the wiremock.reset-mappings-after-each-test property to true.
//        wiremock.resetAll()
    }

    void cleanupSpec() {
        wiremock.shutdown()
    }
}
