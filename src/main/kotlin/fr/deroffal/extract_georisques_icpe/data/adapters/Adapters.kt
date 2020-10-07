package fr.deroffal.extract_georisques_icpe.data.adapters

import fr.deroffal.extract_georisques_icpe.AppProperties
import fr.deroffal.extract_georisques_icpe.data.Etablissement
import org.springframework.stereotype.Component

interface RepositoryAdapter {

    fun matchesDatabase(database: Database): Boolean

    fun createOrUpdate(etablissement: Etablissement)

}

enum class Database {
    POSTGRES
}

@Component
class RepositoryAdapterFactory(
    private val repositoryAdapters: List<RepositoryAdapter>,
    private val properties: AppProperties
) {
    fun getRepositoryAdapter() = repositoryAdapters.first { it.matchesDatabase(properties.db.name) }
}


