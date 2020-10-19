package fr.deroffal.extract_georisques_icpe.util

import org.dbunit.database.IDatabaseConnection
import org.dbunit.database.QueryDataSet
import org.dbunit.dataset.xml.FlatXmlDataSet

class DbUnitExport {

    final String testName

    DbUnitExport(String testName) {
        this.testName = testName
    }

    void export(IDatabaseConnection connection) throws Exception {
        QueryDataSet partialDataSet = new QueryDataSet(connection)

        [
                'ETABLISSEMENT',
                'LOCALISATION',
                'TEXTE',
                'SITUATION',
        ].each {
            partialDataSet.addTable(it)
        }
        FlatXmlDataSet.write(partialDataSet, new FileOutputStream("build/reports/$testName-expected.xml"))
    }
}
