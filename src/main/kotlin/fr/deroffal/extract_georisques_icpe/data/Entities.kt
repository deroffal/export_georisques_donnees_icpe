package fr.deroffal.extract_georisques_icpe.data

import lombok.Getter
import lombok.Setter
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Etablissement(
    @Id @GeneratedValue var id: Long? = null,
    var idInst: String, //idInst
    var nom: String?,//nomInst

//    var region: String?,//regionInst
    var departement: String?,//departementInst
    var commune: String?,//communeInst
    var codePostal: String?,

    var regimeEnVigeur: String?,//regimeInst
    var statutSeveso: String?,//statutInst
    var etatActivite: String?,//etatActiviteInst
    var prioriteNationale: String?//prioNational


    //        var siret: String?
//        ,
//        var adresse1: String?,
//        var adresse2: String?,

//        var codeInsee: String?,
//        var x: String?,
//        var y: String?,
//        var systProj: String?,
//        var codeActiviteInst: String?,
//        var activiteInst: String?,
//        var etatActiviteInst: String?,
//        var nomServiceInspection: String?,
//        var numInspection: String?,
//        var derInspection: String?,
//        var regimeInst: String?,
//        var prioNational: String?,
//        var pied: String?,
//        var statutInst: String?

)
