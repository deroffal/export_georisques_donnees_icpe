package fr.deroffal.extract_georisques_icpe.data

import fr.deroffal.extract_georisques_icpe.batch.rest.EtatActivite
import java.time.LocalDate
import javax.persistence.*

@Entity
data class Etablissement(
    val idInst: String,
    val nom: String,
    val codeSiret: Long?,
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
    val region: String?,
    val departement: String?,
    val commune: String?,
    val codePostal: String?,
    val adresse1: String?,
    val adresse2: String?,
    val codeInsee: String?,
    val x: Long?,
    val y: Long?
) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @OneToOne
    lateinit var etablissement: Etablissement
}

@Entity
data class Texte(
    val dateDoc: LocalDate?,
    val typeDoc: String?,
    val descriptionDoc: String?,
    val urlDoc: String?
) {

    @Id
    @GeneratedValue
    var id: Long = 0

    @ManyToOne
    lateinit var etablissement: Etablissement
}

@Entity
data class Situation(
    val seveso: String,
    val codeNomenclature: String?,
    val alinea: String?,
    val dateAutorisation: LocalDate?,
    val etatActivite: EtatActivite,
    val regime: String?,
    val idRegime: String?,
    val activiteNomenclature: String?,
    val familleNomenclature: String?,
    val volume: String?,
    val unite: String?
) {
    @Id
    @GeneratedValue
    var id: Long = 0

    @ManyToOne
    lateinit var etablissement: Etablissement
}