package cl.camanchaca.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Size {

    private UUID id;
    private BigDecimal initialRange;
    private BigDecimal finalRange;
    private Unit unit;
    private String pieceType;
    private String species;


    public String nameColunm(){
        return new StringBuilder()
                .append(initialRange)
                .append("-")
                .append(finalRange)
                .append(" ")
                .append(unit)
                .toString();
    }

    public static Size ofString(String size){
        String[] splitRanges = size.split("-");
        String finalRange = splitRanges[1].split(" ")[0];
        String unitS = splitRanges[1].split(" ")[1];
        BigDecimal initialRange = new BigDecimal(splitRanges[0]);
        BigDecimal finalRageB = new BigDecimal(finalRange);
        Unit unit = Unit.fromString(unitS);
        return Size.builder()
                .unit(unit)
                .initialRange(initialRange)
                .finalRange(finalRageB)
                .build();
    }

}

