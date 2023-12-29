package cl.camanchaca.parametrization.mappers.bigquery;

import cl.camanchaca.domain.models.Product;
import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.FieldValueList;

import java.math.BigDecimal;

public class ProductBQMapper {

    public static Product toProduct(FieldValueList row) {
        return Product.builder()
                .anomes(rowToInteger(row.get(0)))
                .codigo(rowToInteger(row.get(1)))
                .ajusteSp(
                        stringToBigdecimal(row.get(2))
                )
                .ajusteSpPr(
                        stringToBigdecimal(row.get(3))
                )
                .bloquesBp(
                        stringToBigdecimal(row.get(4))
                )
                .bloquesHarasu(
                        stringToBigdecimal(row.get(5))
                )
                .cDist(
                        stringToBigdecimal(row.get(6))
                )
                .cEmp(
                        stringToBigdecimal(row.get(7))
                )
                .cProcTotal(
                        stringToBigdecimal(row.get(8))
                )
                .caja(
                        rowToString(row.get(9))
                )
                .calibre(rowToString(row.get(10)))
                .calibreTrimd(rowToString(row.get(11)))
                .calidad(rowToString(row.get(12)))
                .calidadLast(rowToString(row.get(13)))
                .calidadObj(rowToString(row.get(14)))
                .color(rowToString(row.get(15)))
                .degrWfe(
                        stringToBigdecimal(row.get(16))
                )
                .descripcion(rowToString(row.get(17)))
                .empPrimario(rowToString(row.get(18)))
                .estado(rowToString(row.get(19)))
                .especie(rowToString(row.get(20)))
                .factorSp(
                        stringToBigdecimal(row.get(21))
                )
                .familia(rowToString(row.get(22)))
                .forma(rowToString(row.get(23)))
                .harina(
                        stringToBigdecimal(row.get(24))
                )
                .mandt(rowToInteger(row.get(25)))
                .mesano(rowToInteger(row.get(26)))
                .netoWfe(
                        stringToBigdecimal(row.get(27))
                )
                .rendimiento(
                        stringToBigdecimal(row.get(28))
                )
                .scale(rowToString(row.get(29)))
                .scrapeMeat(
                        stringToBigdecimal(row.get(30))
                )
                .skin(rowToString(row.get(31)))
                .sobrepeso(
                        stringToBigdecimal(row.get(32))
                )
                .subpWfe(
                        stringToBigdecimal(row.get(33))
                )
                .target(rowToString(row.get(34)))
                .tipo(rowToString(row.get(35)))
                .ultPerProd(rowToString(row.get(36)))
                .wfeBkBp(
                        stringToBigdecimal(row.get(37))
                )
                .wfeBkHarasu(
                        stringToBigdecimal(row.get(38))
                )
                .wfeHarina(
                        stringToBigdecimal(row.get(39))
                )
                .wfeMeat(
                        stringToBigdecimal(row.get(40))
                )
                .build();
    }

    private static BigDecimal stringToBigdecimal(FieldValue value) {
        return value.isNull()
                ? BigDecimal.ZERO
                : new BigDecimal(value.getStringValue()
                .replace(",", "."));
    }

    private static Integer rowToInteger(FieldValue value) {
        return value.isNull()
                ? null
                : Integer.parseInt(value.getStringValue());

    }

    private static String rowToString(FieldValue value) {
        return value.isNull()
                ? null
                : value.getStringValue();

    }

    private ProductBQMapper() {
    }
}
