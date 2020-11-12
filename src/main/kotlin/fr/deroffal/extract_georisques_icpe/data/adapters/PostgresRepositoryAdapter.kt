package fr.deroffal.extract_georisques_icpe.data.adapters

import fr.deroffal.extract_georisques_icpe.data.Etablissement
import fr.deroffal.extract_georisques_icpe.data.Localisation
import fr.deroffal.extract_georisques_icpe.data.Situation
import fr.deroffal.extract_georisques_icpe.data.Texte
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Component
class PostgresRepositoryAdapter(
    private val jdbcTemplate: JdbcTemplate
) : RepositoryAdapter {
    override fun matchesDatabase(database: Database) = database == Database.POSTGRES

    @Transactional
    override fun createOrUpdate(etablissement: Etablissement) {
        val etablissementId = createOrUpdateEtablissement(etablissement)
        createOrUpdateLocalisation(etablissementId, etablissement.localisation)

        if (etablissement.textes.isNotEmpty()) {
            createOrUpdateTextes(etablissementId, etablissement.textes)
        }

        if (etablissement.situations.isNotEmpty()) {
            createOrUpdateSituations(etablissementId, etablissement.situations)
        }

    }

    private fun createOrUpdateEtablissement(etablissement: Etablissement): Long = jdbcTemplate.queryForObject(
        //language=SQL
        """
insert into etablissement (id_inst, code_siret, nom, date_synchronisation)
values (?,?,?, ?)
on conflict (id_inst) do update
    set code_siret              = excluded.code_siret,
        nom                     = excluded.nom,
        date_synchronisation    = excluded.date_synchronisation
returning id
        """.trimIndent(),
        arrayOf(etablissement.idInst, etablissement.codeSiret, etablissement.nom, Timestamp.from(etablissement.dateSynchronisation)),
        Long::class.java
    )

    private fun createOrUpdateLocalisation(etablissementId: Long, localisation: Localisation) =
        jdbcTemplate.update(//language=SQL
            """
insert into localisation (etablissement_id, adresse1, adresse2, code_insee, code_postal, commune, departement, region, x, y, date_synchronisation)
values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
on conflict (etablissement_id) do update
    set adresse1                = excluded.adresse1,
        adresse2                = excluded.adresse2,
        code_insee              = excluded.code_insee,
        code_postal             = excluded.code_postal,
        commune                 = excluded.commune,
        departement             = excluded.departement,
        region                  = excluded.region,
        x                       = excluded.x,
        y                       = excluded.y,
        date_synchronisation    = excluded.date_synchronisation
            """.trimIndent(),
            etablissementId,
            localisation.adresse1,
            localisation.adresse2,
            localisation.codeInsee,
            localisation.codePostal,
            localisation.commune,
            localisation.departement,
            localisation.region,
            localisation.x,
            localisation.y,
            Timestamp.from(localisation.dateSynchronisation)
        )

    private fun createOrUpdateTextes(etablissementId: Long, textes: List<Texte>) =
        jdbcTemplate.update(//language=SQL
            """
    insert into texte(etablissement_id, date_doc, type_doc, description_doc, url_doc, date_synchronisation)
    values ${(textes.joinToString(separator = ", ") { "(?, ?, ?, ?, ?, ?)" })}
    on conflict (etablissement_id, url_doc) do update
            set type_doc                = excluded.type_doc,
                date_doc                = excluded.date_doc,
                description_doc         = excluded.description_doc,
                date_synchronisation    = excluded.date_synchronisation
                """.trimIndent(),
            ArgumentPreparedStatementSetter(textes.map {
                listOf(
                    etablissementId,
                    it.dateDoc,
                    it.typeDoc,
                    it.descriptionDoc,
                    it.urlDoc,
                    Timestamp.from(it.dateSynchronisation)
                )
            }.flatten().toTypedArray())
        )

    //Pas de contrainte d'unicité, on doit faire un annule et remplace.
    private fun createOrUpdateSituations(etablissementId: Long, situations: List<Situation>): Int {
        jdbcTemplate.update("delete from situation where etablissement_id = ?", etablissementId)
        return jdbcTemplate.update(//language=SQL
            """
    insert into situation (etablissement_id, activite_nomenclature, alinea, code_nomenclature, famille_nomenclature,
                           date_autorisation, etat_activite, id_regime, regime, seveso, unite, volume, date_synchronisation)
    values ${situations.joinToString(separator = ", ") { "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" }}
    
                """.trimIndent(),
            ArgumentPreparedStatementSetter(situations.map {
                listOf(
                    etablissementId,
                    it.activiteNomenclature,
                    it.alinea,
                    it.codeNomenclature,
                    it.familleNomenclature,
                    it.dateAutorisation,
                    it.etatActivite.code,
                    it.idRegime,
                    it.regime,
                    it.seveso,
                    it.unite,
                    it.volume,
                    Timestamp.from(it.dateSynchronisation)
                )
            }.flatten().toTypedArray())
        )
    }
}