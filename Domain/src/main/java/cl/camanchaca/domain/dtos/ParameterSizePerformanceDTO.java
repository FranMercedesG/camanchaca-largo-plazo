package cl.camanchaca.domain.dtos;

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
public class ParameterSizePerformanceDTO {

    private UUID id;
    private Integer productId;
    private UUID sizeId;
    private UUID plantId;
    private BigDecimal performance;
    private Boolean status;

}
