package fr.deroffal.extract_georisques_icpe.etablissement.adapter

import fr.deroffal.extract_georisques_icpe.adapter.RepositoryAdapter
import fr.deroffal.extract_georisques_icpe.etablissement.Etablissement

interface EtablissementRepositoryAdapter : RepositoryAdapter {

    fun createOrUpdate(etablissement: Etablissement)

}