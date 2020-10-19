package fr.deroffal.extract_georisques_icpe.batch.importation

import fr.deroffal.extract_georisques_icpe.batch.rest.beans.SituationMapper
import fr.deroffal.extract_georisques_icpe.batch.rest.beans.TexteMapper
import fr.deroffal.extract_georisques_icpe.batch.rest.services.EtablissementRestService
import fr.deroffal.extract_georisques_icpe.batch.rest.services.ParametreExport
import fr.deroffal.extract_georisques_icpe.batch.rest.services.SituationService
import fr.deroffal.extract_georisques_icpe.batch.rest.services.TexteService
import fr.deroffal.extract_georisques_icpe.data.Etablissement
import fr.deroffal.extract_georisques_icpe.service.EtablissementService
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant

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
    private val situationMapper: SituationMapper
) : ItemProcessor<String, Etablissement> {

    override fun process(idInstallation: String): Etablissement? {
        val installation = etablissementRestService.recupererEtablissement(idInstallation)
        val localisation = installation.toLocalisation()
        val textes = texteService.recupererTextes(idInstallation)
        val situation = situationService.recupererSituations(idInstallation)

        return Etablissement(
            idInst = idInstallation,
            nom = installation.nomInst,
            codeSiret = installation.codeSiret,
            localisation = localisation,
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
    override fun write(etablissements: MutableList<out Etablissement>) {
        etablissements.forEach { etablissementService.createOrUpdate(it) }
    }
}

