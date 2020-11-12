package fr.deroffal.extract_georisques_icpe.util.valuecomparer

import org.dbunit.DatabaseUnitException
import org.dbunit.assertion.comparer.value.ValueComparer
import org.dbunit.assertion.comparer.value.ValueComparerTemplateBase
import org.dbunit.dataset.ITable
import org.dbunit.dataset.datatype.DataType

import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.ZoneId

class DateSynchronisationValueComparer extends ValueComparerTemplateBase {

    @Override
    protected boolean isExpected(ITable expectedTable, ITable actualTable, int rowNum, String columnName, DataType dataType, Object expectedValue, Object actualValue) throws DatabaseUnitException {
        if (expectedValue == actualValue) {
            // Dans le cas de données provenant du populate, on a exactement ce à quoi on s'attend
            return true
        }

        //On réupére le timestamp en base. On le formatte en LocalDateTime sur la Zone UTC
        LocalDateTime actual = ((Timestamp) actualValue).toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime()

        //L'expected est en UTC, on le format en LocalDateTime pour le comparer.
        LocalDateTime expected = LocalDateTime.parse(expectedValue, 'yyyy-MM-dd HH:mm:ss.S')
        return actual == expected
    }

    @Override
    protected String getFailPhrase() {
        return "ne correspond pas à"
    }

    static Map<String, ValueComparer> asMap(){
        return  [date_synchronisation: new DateSynchronisationValueComparer()]
    }
}
