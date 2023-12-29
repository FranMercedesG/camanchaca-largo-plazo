package cl.camanchaca.biomass.mappers.bigquery;

import cl.camanchaca.domain.models.biomass.ProjectedBiomassScenario;
import cl.camanchaca.domain.models.biomass.ProyectadaMerkatus;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ProjectedBQBiomassBQMapper {
    public static ProyectadaMerkatus toProyectadaMerkatus(FieldValueList row) {
        return ProyectadaMerkatus
                .builder()
                .year(getStringValueNullable(row.get("Year")))
                .month(getStringValueNullable(row.get("Month")))
                .scenario(getStringValueNullable(row.get("Scenario")))
                .harvestBiomassByMonth(Float.valueOf(getStringValueNullable(row.get("HarvestBiomassByMonth"))))
                .harvestCount(Float.valueOf(getStringValueNullable(row.get("HarvestCount"))))
                .harvestBiomassDividedByHarvestCount(Float.valueOf(getStringValueNullable(row.get("HarvestBiomassDividedByHarvestCount"))))
                .especie(getStringValueNullable(row.get("Especie")))
                .build();
    }


    public static ProjectedBiomassScenario toProjectedBiomassScenario(ProyectadaMerkatus row) {
        return ProjectedBiomassScenario
                .builder()
                .period(getPeriod(row.getMonth(), row.getYear()))
                .averageWeight(row.getHarvestBiomassDividedByHarvestCount())
                .pieceNumber(row.getHarvestCount())
                .kiloWFE(row.getHarvestBiomassByMonth())
                .scenario(row.getScenario())
                .build();
    }

    private static LocalDate getPeriod(String month, String year) {
        Integer monthInt = getMonthInteger(month.toLowerCase());
        return LocalDate.of(Integer.parseInt(year), monthInt, 1);
    }

    private static final Map<String, Integer> monthToInteger = new HashMap<>();

    static {
        monthToInteger.put("january", 1);
        monthToInteger.put("february", 2);
        monthToInteger.put("march", 3);
        monthToInteger.put("april", 4);
        monthToInteger.put("may", 5);
        monthToInteger.put("june", 6);
        monthToInteger.put("july", 7);
        monthToInteger.put("august", 8);
        monthToInteger.put("september", 9);
        monthToInteger.put("october", 10);
        monthToInteger.put("november", 11);
        monthToInteger.put("december", 12);

        monthToInteger.put("enero", 1);
        monthToInteger.put("febrero", 2);
        monthToInteger.put("marzo", 3);
        monthToInteger.put("abril", 4);
        monthToInteger.put("mayo", 5);
        monthToInteger.put("junio", 6);
        monthToInteger.put("julio", 7);
        monthToInteger.put("agosto", 8);
        monthToInteger.put("septiembre", 9);
        monthToInteger.put("octubre", 10);
        monthToInteger.put("noviembre", 11);
        monthToInteger.put("diciembre", 12);
    }

    public static Integer getMonthInteger(String monthName) {
        return monthToInteger.get(monthName);
    }

    private static String getStringValueNullable(FieldValue value) {
        return value.isNull()
                ? null
                : value.getStringValue();
    }

    private static Integer getIntegerValueNullable(FieldValue value) {
        return value.isNull()
                ? null
                : (int) value.getLongValue();
    }
}