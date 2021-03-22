package fr.deroffal.extract_georisques_icpe.adapter

import fr.deroffal.extract_georisques_icpe.AppProperties
import org.springframework.stereotype.Component

@Component
class RepositoryAdapterFactory(
    private val properties: AppProperties,
    val repositoryAdapters: List<RepositoryAdapter>

) {

    fun <T : RepositoryAdapter> getRepositoryAdapterByType(type: Class<T>) : T{
        return repositoryAdapters
            .filterIsInstance(type)
            .first { it.matchesDatabase(properties.db.name) }
    }
}

interface RepositoryAdapter {

    fun matchesDatabase(database: Database): Boolean

}

enum class Database {
    POSTGRES
}




