package fr.deroffal.extract_georisques_icpe.util

import fr.deroffal.extract_georisques_icpe.util.valuecomparer.DateSynchronisationValueComparer
import org.dbunit.Assertion
import org.dbunit.assertion.comparer.value.ValueComparers
import org.dbunit.database.DatabaseDataSourceConnection
import org.dbunit.dataset.ITable
import org.dbunit.dataset.ReplacementTable
import org.dbunit.dataset.SortedTable
import org.dbunit.dataset.filter.DefaultColumnFilter
import org.dbunit.dataset.xml.FlatXmlDataSet
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.ClassPathResource

import javax.sql.DataSource
import java.time.LocalDate

import static java.io.File.separator
import static java.time.format.DateTimeFormatter.ISO_DATE
import static org.dbunit.database.DatabaseConfig.PROPERTY_DATATYPE_FACTORY

trait DbSpec {

    private static final String EXPECTED_DEFAULT_FILE_NAME = 'dbunit/nothing-expected.xml'

    private static final Map<String, List<String>> ADDITIONNAL_FILTERED_COLUMNS_NAMES_BY_TABLE_NAME = [:]
    private static final List<String> FILTERED_COLUMNS_NAMES = []

    @Autowired
    DataSource dataSource

    void verifyDb() {
        ClassPathResource ficherAComparer = new ClassPathResource("dbunit${separator}${this.class.package.name.replace(".", separator)}$separator${specificationContext.currentIteration.name}-expected.xml")
        if (!ficherAComparer.exists()) {
            ficherAComparer = new ClassPathResource(EXPECTED_DEFAULT_FILE_NAME)
        }
        FlatXmlDataSet flatXmlDataSet = new FlatXmlDataSetBuilder().build(ficherAComparer.file.newInputStream())

        DatabaseDataSourceConnection connection = new DatabaseDataSourceConnection(dataSource)
        connection.config.setProperty(PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory())

//        new DbUnitExport(specificationContext.currentIteration.name).export(connection)

        flatXmlDataSet.tableNames.each { tableName ->
            List<String> filteredColumnsNames = (FILTERED_COLUMNS_NAMES + ADDITIONNAL_FILTERED_COLUMNS_NAMES_BY_TABLE_NAME[tableName]).grep()

            ReplacementTable expectedTable = new ReplacementTable(DefaultColumnFilter.excludedColumnsTable(flatXmlDataSet.getTable(tableName), filteredColumnsNames as String[]))
            [
                    '[NULL]'    : null,
                    '[dateJour]': LocalDate.now().format(ISO_DATE)
            ].each { originalObject, replacementObject ->
                expectedTable.addReplacementObject(originalObject, replacementObject)
            }
            ITable sortedExpectedTable = new SortedTable(expectedTable, expectedTable.tableMetaData)

            ITable actualTable = connection.createDataSet().getTable(tableName)
            ITable sortedActualTable = new SortedTable(actualTable, actualTable.tableMetaData)


            Assertion.assertWithValueComparer(
                    sortedExpectedTable,
                    sortedActualTable,
                    ValueComparers.isActualEqualToExpected,
                    DateSynchronisationValueComparer.asMap()
            )
        }

        connection?.close()
    }

}