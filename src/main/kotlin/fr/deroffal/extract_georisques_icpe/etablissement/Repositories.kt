package fr.deroffal.extract_georisques_icpe.etablissement

import org.springframework.data.repository.CrudRepository
import java.time.Instant

interface DeletableByDateSynchronisationNot {
    fun deleteByDateSynchronisationNot(dateSynchronisation: Instant)
}

interface EtablissementRepository : CrudRepository<Etablissement, Long>, DeletableByDateSynchronisationNot
interface TexteRepository : CrudRepository<Texte, Long>, DeletableByDateSynchronisationNot
interface SituationRepository : CrudRepository<Situation, Long>, DeletableByDateSynchronisationNot
interface LocalisationRepository : CrudRepository<Localisation, Long>, DeletableByDateSynchronisationNot


