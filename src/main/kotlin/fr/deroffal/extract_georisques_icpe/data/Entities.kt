package fr.deroffal.extract_georisques_icpe.data

import fr.deroffal.extract_georisques_icpe.batch.rest.beans.EtatActivite
import java.time.LocalDate
import javax.persistence.*

@Entity
data class Etablissement(
    var idInst: String,
    var nom: String,
    var codeSiret: Long?,
    @OneToOne(mappedBy = "etablissement") val localisation: Localisation,
    @OneToMany(mappedBy = "etablissement") val situations: List<Situation>,
    @OneToMany(mappedBy = "etablissement") val textes: List<Texte>
) {
    @Id
    @GeneratedValue
    var id: Long = 0
}

@Entity
data class Localisation(
    var region: String?,
    var departement: String?,
    var commune: String?,
    var codePostal: String?,
    var adresse1: String?,
    var adresse2: String?,
    var codeInsee: String?,
    var x: Long?,
    var y: Long?
) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @OneToOne
    lateinit var etablissement: Etablissement
}

@Entity
data class Texte(
    var dateDoc: LocalDate?,
    var typeDoc: String?,
    var descriptionDoc: String?,
    var urlDoc: String?
) {

    @Id
    @GeneratedValue
    var id: Long = 0

    @ManyToOne
    lateinit var etablissement: Etablissement
}

@Entity
data class Situation(
    var seveso: String,
    var codeNomenclature: String?,
    var alinea: String?,
    var dateAutorisation: LocalDate?,
    var etatActivite: EtatActivite,
    var regime: String?,
    var idRegime: String?,
    var activiteNomenclature: String?,
    var familleNomenclature: String?,
    var volume: String?,
    var unite: String?
) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @ManyToOne
    lateinit var etablissement: Etablissement
}