package fr.deroffal.extract_georisques_icpe.service

import fr.deroffal.extract_georisques_icpe.data.Etablissement
import fr.deroffal.extract_georisques_icpe.data.adapters.RepositoryAdapterFactory
import org.springframework.stereotype.Service

@Service
class EtablissementService(
    private val repositoryAdapterFactory: RepositoryAdapterFactory
) {

    fun createOrUpdate(etablissement: Etablissement) = repositoryAdapterFactory.getRepositoryAdapter()
        .createOrUpdate(etablissement)
}