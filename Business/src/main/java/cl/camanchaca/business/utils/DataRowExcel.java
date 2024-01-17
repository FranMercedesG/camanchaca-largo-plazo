package cl.camanchaca.business.utils;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder(toBuilder = true)
public class DataRowExcel {
    private Integer productId;
    private String specie;
    private Map<String, Double> values;
}
