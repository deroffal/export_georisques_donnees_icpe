package fr.deroffal.extract_georisques_icpe.batch.importation

import fr.deroffal.extract_georisques_icpe.batch.rest.beans.*
import fr.deroffal.extract_georisques_icpe.batch.rest.services.EtablissementRestService
import fr.deroffal.extract_georisques_icpe.batch.rest.services.SituationService
import fr.deroffal.extract_georisques_icpe.batch.rest.services.TexteService
import fr.deroffal.extract_georisques_icpe.etablissement.Etablissement
import fr.deroffal.extract_georisques_icpe.etablissement.Localisation
import fr.deroffal.extract_georisques_icpe.etablissement.Situation
import fr.deroffal.extract_georisques_icpe.etablissement.Texte
import spock.lang.Specification

import java.time.Instant
import java.time.LocalDate

import static fr.deroffal.extract_georisques_icpe.batch.rest.beans.EtatActivite.EN_FONCTIONNEMENT

class BatchImportEtablissementProcessorSpec extends Specification {

    void "process construit un Etablissement en appelant les services REST"() {
        given:
        String idInstallation = 'id_inst'
        and:
        EtablissementDto etablissement = new EtablissementDto(
                'idInst', 'nomInst', 123L, 'regionInst',
                'departementInst', 'communeInst', 'adresse1', 'adresse2'
                , 'codePostal', 'codeInsee', 1L, 2L,
                'systProj', 'codeActiviteInst', 'activiteInst', 'etatActiviteInst',
                'nomServiceInspection', 'numInspection', 'derInspection', 'regimeInst',
                'prioNational', 'pied', 'statutInst'
        )
        EtablissementRestService etablissementRestService = GroovyMock(EtablissementRestService) {
            recupererEtablissement(idInstallation) >> etablissement
        }
        and:
        TexteDto texteDto1 = new TexteDto(LocalDate.now(), 'typeDoc1', 'descriptionDoc1', 'urlDoc1')
        TexteDto texteDto2 = new TexteDto(LocalDate.now(), 'typeDoc2', 'descriptionDoc2', 'urlDoc2')
        TexteService texteService = GroovyMock(TexteService) {
            recupererTextes(idInstallation) >> [texteDto1, texteDto2]
        }
        Texte texte1 = new Texte(LocalDate.now(), 'typeDoc1', 'descriptionDoc1', 'urlDoc1', Instant.now())
        Texte texte2 = new Texte(LocalDate.now(), 'typeDoc2', 'descriptionDoc2', 'urlDoc2', Instant.now())
        TexteMapper texteMapper = GroovyMock(TexteMapper) {
            toEntity(texteDto1) >> texte1
            toEntity(texteDto2) >> texte2
        }
        and:
        SituationDto situationDto = new SituationDto('seveso', 'codeNomenclature', 'alinea', LocalDate.now(), 1, 'regime', 'idRegime', 'activiteNomenclatureInst', 'familleNomenclature', 'volumeInst', 'unite')
        SituationService situationService = GroovyMock(SituationService) {
            recupererSituations(idInstallation) >> [situationDto]
        }
        Situation situation = new Situation('seveso', 'codeNomenclature', 'alinea', LocalDate.now(), EN_FONCTIONNEMENT, 'regime', 'idRegime', 'activiteNomenclatureInst', 'familleNomenclature', 'volumeInst', 'unite', Instant.now())
        SituationMapper localisationMapper = GroovyMock(SituationMapper) {
            toEntity(situationDto) >> situation
        }

        BatchImportEtablissementProcessor processor = new BatchImportEtablissementProcessor(etablissementRestService, situationService, texteService, texteMapper, localisationMapper, new Date())

        when:
        Etablissement e = processor.process(idInstallation)

        then:
        e.idInst == idInstallation
        e.nom == 'nomInst'
        e.codeSiret == 123L
        and:
        Localisation localisation = e.localisation
        localisation.codeInsee == 'codeInsee'
        and:
        e.textes == [texte1, texte2]
        and:
        e.situations == [situation]
    }

}