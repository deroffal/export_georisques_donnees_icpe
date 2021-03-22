package fr.deroffal.extract_georisques_icpe.etablissement

import fr.deroffal.extract_georisques_icpe.adapter.RepositoryAdapterFactory
import fr.deroffal.extract_georisques_icpe.etablissement.adapter.EtablissementRepositoryAdapter
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

    fun createOrUpdate(etablissement: Etablissement) = repositoryAdapterFactory
        .getRepositoryAdapterByType(EtablissementRepositoryAdapter::class.java)
        .createOrUpdate(etablissement)

    fun deleteByDateSynchronisationNotEquals(dateSynchronisation: Instant) =
        mutableListOf<DeletableByDateSynchronisationNot>(
            localisationRepository, situationRepository, texteRepository, etablissementRepository
        ).forEach { it.deleteByDateSynchronisationNot(dateSynchronisation) }

}