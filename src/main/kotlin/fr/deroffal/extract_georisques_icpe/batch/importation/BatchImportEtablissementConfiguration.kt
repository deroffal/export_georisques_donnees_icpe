package fr.deroffal.extract_georisques_icpe.batch.importation

import fr.deroffal.extract_georisques_icpe.data.Etablissement
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BatchImportEtablissementConfiguration(
    private val jobs: JobBuilderFactory,
    private val steps: StepBuilderFactory
) {

    @Bean
    fun importJob(importerDepuisGeorisquesStep: Step): Job {
        return jobs["importJob"]
            .incrementer(RunIdIncrementer())
            .start(importerDepuisGeorisquesStep).on("*").end().build()
            .build()
    }

    @Bean
    fun importerDepuisGeorisquesStep(
        reader: BatchImportEtablissementReader,
        processor: BatchImportEtablissementProcessor,
        writer: BatchImportEtablissementWriter
    ) = steps["importerDepuisGeorisquesStep"]
        .chunk<String, Etablissement>(10)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build()
}