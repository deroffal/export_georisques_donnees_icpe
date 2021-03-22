package fr.deroffal.extract_georisques_icpe.batch.importation

import fr.deroffal.extract_georisques_icpe.batch.rest.beans.SituationMapper
import fr.deroffal.extract_georisques_icpe.batch.rest.beans.TexteMapper
import fr.deroffal.extract_georisques_icpe.batch.rest.services.EtablissementRestService
import fr.deroffal.extract_georisques_icpe.batch.rest.services.ParametreExport
import fr.deroffal.extract_georisques_icpe.batch.rest.services.SituationService
import fr.deroffal.extract_georisques_icpe.batch.rest.services.TexteService
import fr.deroffal.extract_georisques_icpe.etablissement.Etablissement
import fr.deroffal.extract_georisques_icpe.etablissement.EtablissementService
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*

@Component
@StepScope
class BatchImportEtablissementReader(
    private val etablissementRestService: EtablissementRestService,
    @Value("#{jobParameters['parametreGeographiqueExport']}") private val parametreGeographiqueExport: String?,
    @Value("#{jobParameters['exclureSeveso']}") private val exclureSeveso: String?
) : ItemReader<String> {

    var ids: List<String>? = null
    var index = 0

    override fun read(): String? {
        if (ids == null) {
            ids = listerIdInstallation()
        }
        val e = ids as List<String>
        if (index < e.size) {
            return e[index++]
        }
        return null
    }

    private fun listerIdInstallation(): List<String> {
        val params = ParametreExport.Builder()
            .parametreGeographiqueExport(parametreGeographiqueExport)
            .exclureSeveso(exclureSeveso)
            .build()

        return etablissementRestService.listerIdInstallation(params)
    }
}

@Component
@StepScope
class BatchImportEtablissementProcessor(
    private val etablissementRestService: EtablissementRestService,
    private val situationService: SituationService,
    private val texteService: TexteService,
    private val texteMapper: TexteMapper,
    private val situationMapper: SituationMapper,
    @Value("#{jobParameters['dateSynchronisation']}") private val dateSynchronisation: Date
) : ItemProcessor<String, Etablissement> {

    override fun process(idInstallation: String): Etablissement? {
        val instantSynchronisation = dateSynchronisation.toInstant()

        val installation = etablissementRestService.recupererEtablissement(idInstallation)
        val localisation = installation.toLocalisation(dateSynchronisation)
        val textes = texteService.recupererTextes(idInstallation).onEach { it.dateSynchronisation = instantSynchronisation }
        val situation = situationService.recupererSituations(idInstallation).onEach { it.dateSynchronisation = instantSynchronisation }

        return Etablissement(
            idInst = idInstallation,
            nom = installation.nomInst,
            codeSiret = installation.codeSiret,
            localisation = localisation,
            dateSynchronisation = instantSynchronisation,
            textes = textes.map { texteMapper.toEntity(it) }.toMutableList(),
            situations = situation.map { situationMapper.toEntity(it) }.toMutableList()
        )
    }
}

@Component
@StepScope
class BatchImportEtablissementWriter(
    private val etablissementService: EtablissementService
) : ItemWriter<Etablissement> {

    //TODO https://www.baeldung.com/kotlin-logging
    private val logger = LoggerFactory.getLogger(javaClass)

    override fun write(etablissements: MutableList<out Etablissement>) {
        etablissements.forEach { etablissementService.createOrUpdate(it) }
        logger.debug("Création de ${etablissements.size} établissements !")
    }
}

@Component
@StepScope
class SupprimerDonneesNonMiseAJourTasklet(
    private val etablissementService: EtablissementService,
    @Value("#{jobParameters['dateSynchronisation']}") private val dateSynchronisation: Date
) : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        etablissementService.deleteByDateSynchronisationNotEquals(dateSynchronisation.toInstant())
        return RepeatStatus.FINISHED
    }
}