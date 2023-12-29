package cl.camanchaca.business.generic;

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
public class UpdateOptimizationBodyRequest {
    private UUID orderMonthlyPlanificationId;
    private BigDecimal kilosWfe;
}
