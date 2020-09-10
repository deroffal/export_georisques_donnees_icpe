package fr.deroffal.extract_georisques_icpe.batch.import

import fr.deroffal.extract_georisques_icpe.batch.rest.EtablissementService
import fr.deroffal.extract_georisques_icpe.batch.rest.ParametreExport
import fr.deroffal.extract_georisques_icpe.batch.rest.SituationService
import fr.deroffal.extract_georisques_icpe.batch.rest.TexteService
import fr.deroffal.extract_georisques_icpe.data.Etablissement
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
    private val etablissementService: EtablissementService,
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

        return etablissementService.listerIdInstallation(params)
    }
}

@Component
@StepScope
class BatchImportEtablissementProcessor(
    private val etablissementService: EtablissementService,
    private val situationService: SituationService,
    private val texteService: TexteService
) : ItemProcessor<String, Etablissement> {

    override fun process(idInstallation: String): Etablissement? {
        val installation = etablissementService.recupererEtablissement(idInstallation)
        val localisation = installation.toLocalisation()
        val textes = texteService.recupererTextes(idInstallation)
        val situation = situationService.recupererSituations(idInstallation)

        return Etablissement(
            idInst = idInstallation,
            nom = installation.nomInst!!,//TODO à valider
            codeSiret = installation.codeSiret,
            localisation = localisation,
            textes = textes.map { it.toTexte() }.toMutableList(),
            situations = situation.map { it.toSituation() }.toMutableList()
        )
    }
}

@Component
@StepScope
class BatchImportEtablissementWriter : ItemWriter<Etablissement> {
    override fun write(items: MutableList<out Etablissement>) {
        println(Instant.now())
//        TODO("Not yet implemented")
    }
}

