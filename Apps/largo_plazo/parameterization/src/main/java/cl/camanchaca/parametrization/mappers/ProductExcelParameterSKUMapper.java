package cl.camanchaca.parametrization.mappers;

import cl.camanchaca.domain.models.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class ProductExcelParameterSKUMapper {
    private ProductExcelParameterSKUMapper() {
    }

    private static final Map<Integer, Method> INDEX_TO_SETTER_MAP = createIndexToSetterMap();

    private static Map<Integer, Method> createIndexToSetterMap() {
        Map<Integer, Method> map = new HashMap<>();
        try {
            Class<Product> productClass = Product.class;
            map.put(0, productClass.getMethod("setAnomes", Integer.class));
            map.put(1, productClass.getMethod("setCodigo", Integer.class));
            map.put(2, productClass.getMethod("setAjusteSp", BigDecimal.class));
            map.put(3, productClass.getMethod("setAjusteSpPr", BigDecimal.class));
            map.put(4, productClass.getMethod("setBloquesBp", BigDecimal.class));
            map.put(5, productClass.getMethod("setBloquesHarasu", BigDecimal.class));
            map.put(6, productClass.getMethod("setCDist", BigDecimal.class));
            map.put(7, productClass.getMethod("setCEmp", BigDecimal.class));
            map.put(8, productClass.getMethod("setCProcTotal", BigDecimal.class));
            map.put(9, productClass.getMethod("setCaja", String.class));
            map.put(10, productClass.getMethod("setCalibre", String.class));
            map.put(11, productClass.getMethod("setCalibreTrimd", String.class));
            map.put(12, productClass.getMethod("setCalidad", String.class));
            map.put(13, productClass.getMethod("setCalidadLast", String.class));
            map.put(14, productClass.getMethod("setCalidadObj", String.class));
            map.put(15, productClass.getMethod("setColor", String.class));
            map.put(16, productClass.getMethod("setDegrWfe", BigDecimal.class));
            map.put(17, productClass.getMethod("setDescripcion", String.class));
            map.put(18, productClass.getMethod("setEmpPrimario", String.class));
            map.put(19, productClass.getMethod("setEstado", String.class));
            map.put(20, productClass.getMethod("setEspecie", String.class));
            map.put(21, productClass.getMethod("setFactorSp", BigDecimal.class));
            map.put(22, productClass.getMethod("setFamilia", String.class));
            map.put(23, productClass.getMethod("setForma", String.class));
            map.put(34, productClass.getMethod("setTarget", String.class));
            map.put(24, productClass.getMethod("setHarina", BigDecimal.class));
            map.put(25, productClass.getMethod("setMandt", Integer.class));
            map.put(26, productClass.getMethod("setMesano", Integer.class));
            map.put(27, productClass.getMethod("setNetoWfe", BigDecimal.class));
            map.put(28, productClass.getMethod("setRendimiento", BigDecimal.class));
            map.put(29, productClass.getMethod("setScale", String.class));
            map.put(30, productClass.getMethod("setScrapeMeat", BigDecimal.class));
            map.put(31, productClass.getMethod("setSkin", String.class));
            map.put(32, productClass.getMethod("setSobrepeso", BigDecimal.class));
            map.put(33, productClass.getMethod("setSubpWfe", BigDecimal.class));
            map.put(35, productClass.getMethod("setTipo", String.class));
            map.put(36, productClass.getMethod("setUltPerProd", String.class));
            map.put(37, productClass.getMethod("setWfeBkBp", BigDecimal.class));
            map.put(38, productClass.getMethod("setWfeBkHarasu", BigDecimal.class));
            map.put(39, productClass.getMethod("setWfeHarina", BigDecimal.class));
            map.put(40, productClass.getMethod("setWfeMeat", BigDecimal.class));

        } catch (Exception e) {
            log.error("Error creating index to setter map", e);
        }
        return map;
    }

    public static Product rowToProduct(Iterator<Cell> cellIterator, Product product) throws InvocationTargetException, IllegalAccessException {
        DataFormatter dataFormatter = new DataFormatter();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            int columnIndex = cell.getColumnIndex();
            Method setterMethod = INDEX_TO_SETTER_MAP.get(columnIndex);
            String cellValue = dataFormatter.formatCellValue(cell);

            if (Objects.nonNull(setterMethod)
                    && Objects.nonNull(cellValue)
                    && cellValue.length() > 0
            ) {
                Class<?> parameterType = setterMethod.getParameterTypes()[0];

                if (parameterType.equals(Integer.class)) {
                    String formatValue = cellValue.replaceFirst(",", ".");
                    setterMethod.invoke(product, Integer.valueOf(formatValue));
                } else if (parameterType.equals(BigDecimal.class)) {
                    String formatValue = cellValue.replaceFirst(",", ".");
                    setterMethod.invoke(product, new BigDecimal(formatValue));
                } else {
                    setterMethod.invoke(product, cellValue);
                }

            }
        }

        return product;
    }
}
