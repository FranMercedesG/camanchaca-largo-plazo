package cl.camanchaca.biomass.mappers.bigquery;

import cl.camanchaca.domain.models.biomass.RealBiomass;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;

public class RealBiomassBQMapper {

    public static RealBiomass toRealBiomass(FieldValueList row) {
        return RealBiomass.builder()
                .hu(getStringValueNullable(row.get("HU")))
                .size(getStringValueNullable(row.get("Calibre")))
                .plant(getStringValueNullable(row.get("Descripcion1")))
                .quality(getStringValueNullable(row.get("Calidad")))
                .pieces(
                        Double.valueOf(getStringValueNullable(row.get("Piezas")))
                )
                .weight(
                        Double.valueOf(getStringValueNullable(row.get("PesoKilos")))
                )
                .specie(getStringValueNullable(row.get("Especie")))
                .build();
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
    private RealBiomassBQMapper() {
    }
}
