package fr.deroffal.extract_georisques_icpe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties(AppProperties::class)
@SpringBootApplication
class ExtractGeorisquesIcpeApplication

fun main(args: Array<String>) {
    runApplication<ExtractGeorisquesIcpeApplication>(*args)
}
