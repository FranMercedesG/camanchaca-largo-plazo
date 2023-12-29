package cl.camanchaca.orders.mappers;

import cl.camanchaca.domain.models.biomass.ProyectadaMerkatus;
import cl.camanchaca.domain.models.demand.Freight;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;

public class BigQueryMapper {

    public static Freight toFreight(FieldValueList row) {

        return Freight.builder()
                .port(getStringValueNullable(row.get("PuertoDestino")))
                .sectorNum(getStringValueNullable(row.get("NumeroSector")))
                .formaGrupo(getStringValueNullable(row.get("FormaGrupo")))
                .incoterms(getStringValueNullable(row.get("Incoterms")))
                .freightValue(Float.valueOf(getStringValueNullable(row.get("FleteReferencia"))))
                .build();

    }

    private static String getStringValueNullable(FieldValue value) {
        return value.isNull()
                ? null
                : value.getStringValue();
    }

}
