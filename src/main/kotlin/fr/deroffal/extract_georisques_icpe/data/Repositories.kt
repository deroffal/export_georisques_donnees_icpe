package fr.deroffal.extract_georisques_icpe.data

import org.springframework.data.repository.CrudRepository

interface EtablissementRepository : CrudRepository<Etablissement, Long>
