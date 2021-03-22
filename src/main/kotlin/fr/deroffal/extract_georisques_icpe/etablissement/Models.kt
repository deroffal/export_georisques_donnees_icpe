package fr.deroffal.extract_georisques_icpe.etablissement

import fr.deroffal.extract_georisques_icpe.batch.rest.beans.EtatActivite
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy.IGNORE
import java.time.Instant
import java.time.LocalDate

data class EtablissementModel(
    var idInst: String,
    var nom: String,
    var codeSiret: Long?,
    var dateSynchronisation: Instant,
    val localisation: LocalisationModel,
    val situations: List<SituationModel>,
    val textes: List<TexteModel>
)

data class LocalisationModel(
    var region: String?,
    var departement: String?,
    var commune: String?,
    var codePostal: String?,
    var adresse1: String?,
    var adresse2: String?,
    var codeInsee: String?,
    var x: Long?,
    var y: Long?,
    var dateSynchronisation: Instant
)

data class TexteModel(
    var dateDoc: LocalDate?,
    var typeDoc: String?,
    var descriptionDoc: String?,
    var urlDoc: String?,
    var dateSynchronisation: Instant
)

data class SituationModel(
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
    var unite: String?,
    var dateSynchronisation: Instant
)

@Mapper(
    componentModel = "spring",
    unmappedSourcePolicy = IGNORE
)
interface EtablissementModelMapper {
    fun toModel(etablissement: MutableIterable<Etablissement>): MutableIterable<EtablissementModel>
}