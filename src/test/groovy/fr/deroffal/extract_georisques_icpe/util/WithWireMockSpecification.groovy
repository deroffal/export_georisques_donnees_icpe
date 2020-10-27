package fr.deroffal.extract_georisques_icpe.util

import com.github.tomakehurst.wiremock.WireMockServer
import fr.deroffal.extract_georisques_icpe.batch.rest.HttpBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.contract.wiremock.WireMockSpring
import spock.lang.Shared

////https://cloud.spring.io/spring-cloud-contract/reference/html/project-features.html#features-wiremock
trait WithWireMockSpecification {

    @Shared
    private static WireMockServer wiremock = new WireMockServer(WireMockSpring.options().dynamicPort())

    @Autowired
    private HttpBuilder httpBuilder

    void setupSpec() {
        wiremock.start()
    }

    void setup(){
        String baseUrl = "http://localhost:${wiremock.port()}"
        System.properties['app.rest.baseUrl'] = baseUrl
    }

    void cleanup() {
        //The registered WireMock server is reset after each test class, however, if you need to reset it after each test method, just set the wiremock.reset-mappings-after-each-test property to true.
        wiremock.resetAll()
    }

    void cleanupSpec() {
        wiremock.shutdown()
    }

}
