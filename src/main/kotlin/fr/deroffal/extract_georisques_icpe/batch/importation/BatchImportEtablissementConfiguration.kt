package fr.deroffal.extract_georisques_icpe.batch.importation

import fr.deroffal.extract_georisques_icpe.etablissement.Etablissement
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.job.DefaultJobParametersValidator
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BatchImportEtablissementConfiguration(
    private val jobs: JobBuilderFactory,
    private val steps: StepBuilderFactory
) {

    @Bean
    fun importJob(
        importerDepuisGeorisquesStep: Step,
        supprimerDonneesNonMiseAJourStep: Step
    ): Job {
        return jobs["importJob"]
            .validator(DefaultJobParametersValidator(arrayOf("dateSynchronisation"), arrayOf("parametreGeographiqueExport", "exclureSeveso")))
            .incrementer(RunIdIncrementer())
            .start(importerDepuisGeorisquesStep)
            .on("COMPLETED")
            .to(supprimerDonneesNonMiseAJourStep)
            .on("COMPLETED")
            .end()
            .from(importerDepuisGeorisquesStep).on("*").fail()
            .from(supprimerDonneesNonMiseAJourStep).on("*").fail()
            .build()
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

    @Bean
    fun supprimerDonneesNonMiseAJourStep(
        supprimerDonneesNonMiseAJourTasklet: SupprimerDonneesNonMiseAJourTasklet
    ) = steps["supprimerDonneesNonMiseAJourStep"]
        .tasklet(supprimerDonneesNonMiseAJourTasklet)
        .build()
}