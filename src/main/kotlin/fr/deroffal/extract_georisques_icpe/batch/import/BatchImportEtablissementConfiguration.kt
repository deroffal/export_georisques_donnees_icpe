package fr.deroffal.extract_georisques_icpe.batch.import

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BatchImportEtablissementConfiguration(
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory
) {

    @Bean
    fun import(importerDepuisGeorisquesStep: Step): Job {
        return jobBuilderFactory["import"]
            .incrementer(RunIdIncrementer())
            .flow(importerDepuisGeorisquesStep)
            .end()
            .build()
    }

    @Bean
    fun importerDepuisGeorisquesStep(
        importerEtablissementTasklet: ImporterEtablissementTasklet
    ) = stepBuilderFactory.get("importerDepuisGeorisquesStep")
        .tasklet(importerEtablissementTasklet)
        .build()

    @Bean
    fun importerEtablissementTasklet() = ImporterEtablissementTasklet()
}