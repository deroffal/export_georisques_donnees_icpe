package fr.deroffal.export

enum class ParametreExport(val clef: String, val code: Int) {

    NANTES("commune", 44109),
    LOIRE_ATLANTIQUE("departement", 44),
    PAYS_DE_LA_LOIRE("region", 52);

    fun asUrlParam() = "${this.clef}=${this.code}"
}
