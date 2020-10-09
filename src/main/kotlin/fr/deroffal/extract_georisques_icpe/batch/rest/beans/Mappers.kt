package fr.deroffal.extract_georisques_icpe.batch.rest.beans

import fr.deroffal.extract_georisques_icpe.data.Situation
import fr.deroffal.extract_georisques_icpe.data.Texte
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings
import java.util.*

@Mapper(componentModel = "spring")
interface TexteMapper {

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "etablissement", ignore = true)
    )
    fun toEntity(texte: TexteDto): Texte
}

@Mapper(componentModel = "spring")
abstract class SituationMapper {

    @Mappings(
        Mapping(target = "id", ignore = true),
        Mapping(target = "etablissement", ignore = true),
        Mapping(source = "activiteNomenclatureInst", target = "activiteNomenclature"),
        Mapping(source = "volumeInst", target = "volume")
    )
    abstract fun toEntity(situation: SituationDto): Situation

    fun mapEtatActivite(etatActivite: Int): EtatActivite =
        Optional.ofNullable(EtatActivite.values().find { it.code == etatActivite })
            .orElseThrow { IllegalArgumentException("Etat activité inconnu !") }
}