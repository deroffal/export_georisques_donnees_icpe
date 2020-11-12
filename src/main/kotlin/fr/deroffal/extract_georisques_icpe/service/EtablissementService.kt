package fr.deroffal.extract_georisques_icpe.service

import fr.deroffal.extract_georisques_icpe.data.*
import fr.deroffal.extract_georisques_icpe.data.adapters.RepositoryAdapterFactory
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class EtablissementService(
    private val repositoryAdapterFactory: RepositoryAdapterFactory,
    private val etablissementRepository: EtablissementRepository,
    private val texteRepository: TexteRepository,
    private val situationRepository: SituationRepository,
    private val localisationRepository: LocalisationRepository
) {

    fun createOrUpdate(etablissement: Etablissement) = repositoryAdapterFactory.getRepositoryAdapter()
        .createOrUpdate(etablissement)

    fun deleteByDateSynchronisationNotEquals(dateSynchronisation: Instant) =
        mutableListOf<DeletableByDateSynchronisationNot>(
            localisationRepository, situationRepository, texteRepository, etablissementRepository
        ).forEach { it.deleteByDateSynchronisationNot(dateSynchronisation) }

}