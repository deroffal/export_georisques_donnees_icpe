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

@Component
@StepScope
class BatchImportEtablissementReader(
    private val etablissementService: EtablissementService,
    @Value("#{jobParameters['parametreGeographiqueExport']}") private val parametreGeographiqueExport: String?,
    @Value("#{jobParameters['exclureSeveso']}") private val exclureSeveso: String?
) : ItemReader<Etablissement> {

    var etablissements: List<Etablissement>? = null
    var index = 0

    override fun read(): Etablissement? {
        if (etablissements == null) {
            etablissements = listerEtablissement()
        }
        val e = etablissements as List<Etablissement>
        if (index < e.size) {
            index++
            return e[index]
        }
        return null
    }

    private fun listerEtablissement(): List<Etablissement> {
        val params = ParametreExport.Builder()
            .parametreGeographiqueExport(parametreGeographiqueExport)
            .exclureSeveso(exclureSeveso)
            .build()

        return etablissementService.listerEtablissements(params)
    }
}

@Component
@StepScope
class BatchImportEtablissementProcessor(
    private val etablissementService: EtablissementService,
    private val situationService: SituationService,
    private val texteService: TexteService
) : ItemProcessor<Etablissement, Etablissement> {

    override fun process(etablissement: Etablissement): Etablissement? {
        val numeroEtablissement = etablissement.idInst.replace('.', '-')
        val infosEtablissement = etablissementService.recupererEtablissement(numeroEtablissement)
        val situation = situationService.recupererSituations(numeroEtablissement)
        val textes = texteService.recupererTextes(numeroEtablissement)
        return etablissement
    }
}

@Component
@StepScope
class BatchImportEtablissementWriter : ItemWriter<Etablissement> {
    override fun write(items: MutableList<out Etablissement>) {
        TODO("Not yet implemented")
    }
}

