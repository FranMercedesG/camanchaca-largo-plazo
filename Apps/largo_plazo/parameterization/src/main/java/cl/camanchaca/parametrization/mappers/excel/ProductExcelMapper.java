package cl.camanchaca.parametrization.mappers.excel;

import cl.camanchaca.domain.models.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class ProductExcelMapper {

    public static List<String> toRow(Product data) {
        return List.of(
                data.getAnomes().toString(),
                data.getCodigo().toString(),
                bigdecimalToString(data.getAjusteSp()),
                bigdecimalToString(data.getAjusteSpPr()),
                bigdecimalToString(data.getBloquesBp()),
                bigdecimalToString(data.getBloquesHarasu()),
                bigdecimalToString(data.getCDist()),
                bigdecimalToString(data.getCEmp()),
                bigdecimalToString(data.getCProcTotal()),
                stringNulleableToString(data.getCaja()),
                stringNulleableToString(data.getCalibre()),
                stringNulleableToString(data.getCalibreTrimd()),
                stringNulleableToString(data.getCalidad()),
                stringNulleableToString(data.getCalidadLast()),
                stringNulleableToString(data.getCalidadObj()),
                stringNulleableToString(data.getColor()),
                bigdecimalToString(data.getDegrWfe()),
                stringNulleableToString(data.getDescripcion()),
                stringNulleableToString(data.getEmpPrimario()),
                stringNulleableToString(data.getEstado()),
                stringNulleableToString(data.getEspecie()),
                bigdecimalToString(data.getFactorSp()),
                stringNulleableToString(data.getFamilia()),
                stringNulleableToString(data.getForma()),
                bigdecimalToString(data.getHarina()),
                integerToString(data.getMandt()),
                integerToString(data.getMesano()),
                bigdecimalToString(data.getNetoWfe()),
                bigdecimalToString(data.getRendimiento()),
                stringNulleableToString(data.getScale()),
                bigdecimalToString(data.getSobrepeso()),
                bigdecimalToString(data.getSubpWfe()),
                stringNulleableToString(data.getTarget()),
                stringNulleableToString(data.getTipo()),
                stringNulleableToString(data.getUltPerProd()),
                bigdecimalToString(data.getWfeBkBp()),
                bigdecimalToString(data.getWfeBkHarasu()),
                bigdecimalToString(data.getWfeHarina()),
                bigdecimalToString(data.getWfeMeat())
        );
    }


    public static List<String> getHeaders() {
        return List.of(
                "año-mes",
                "code",
                "Ajuste sp",
                "Ajuste sp pr",
                "Bloques Bp",
                "Bloques Harasy",
                "CDist",
                "CEmp",
                "CProcTotal",
                "Caja",
                "Calibre",
                "Calibre Trimd",
                "Calidad",
                "CalidadLast",
                "CalidadObj",
                "Color",
                "DegrWfe",
                "Descripcion",
                "EmpPrimario",
                "Estado",
                "Especie",
                "Factor sp",
                "Familia",
                "Forma",
                "Harina",
                "Mandt",
                "mes-año",
                "neto wfe",
                "Rendimiento",
                "Scale",
                "Sobrepeso",
                "SubpWfe",
                "Target",
                "Tipo",
                "UltPerProd",
                "wfe bk bp",
                "wfe harina",
                "wfe meat"
        );
    }


    private static String bigdecimalToString(BigDecimal data) {
        return Objects.isNull(data) ? ""
                : data.toString();
    }

    private static String integerToString(Integer data) {
        return Objects.isNull(data) ? ""
                : data.toString();
    }

    private static String stringNulleableToString(String data){
        return Objects.requireNonNullElse(data, "");
    }

    private ProductExcelMapper() {
    }
}
