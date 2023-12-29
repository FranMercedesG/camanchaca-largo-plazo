package cl.camanchaca.biomass.mappers.bigquery;

import cl.camanchaca.domain.models.biomass.AvailableBiomass;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;

public class AvailableBiomassBQMapper {

    public static AvailableBiomass toAvailableBiomass(FieldValueList row) {
        return AvailableBiomass.builder()
                .hu(getStringValueNullable(row.get("HU")))
                .externalHu(getStringValueNullable(row.get("HU_Externa")))
                .type(getStringValueNullable(row.get("Tipo")))
                .size(getStringValueNullable(row.get("Calibre")))
                .quality(getStringValueNullable(row.get("Calidad")))
                .manufacturingOrder(getStringValueNullable(row.get("Order_Fabricacion")))
                .batch(getStringValueNullable(row.get("Lote")))
                .folio(getStringValueNullable(row.get("Folio")))
                .center(getStringValueNullable(row.get("Centro")))
                .fecpro(getStringValueNullable(row.get("Fecpro")))
                .permanency(getIntegerValueNullable(row.get("Permanecia")))
                .pieces(
                        Double.valueOf(getStringValueNullable(row.get("Piezas")))
                )
                .weight(
                        Double.valueOf(getStringValueNullable(row.get("Peso")))
                )
                .specie(getStringValueNullable(row.get("Especie")))
                .header(getStringValueNullable(row.get("Cabecera")))
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
    private AvailableBiomassBQMapper() {
    }
}
