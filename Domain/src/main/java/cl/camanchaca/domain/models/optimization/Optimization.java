package cl.camanchaca.domain.models.optimization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Optimization {

    private UUID unrestricterDemandId;
    private UUID sizeId;
    private LocalDate period;
    private BigDecimal kiloWfe;
    private Integer codigo;
    private String sizeName;
    private UUID orderMonthlyPlanificationId;

}
