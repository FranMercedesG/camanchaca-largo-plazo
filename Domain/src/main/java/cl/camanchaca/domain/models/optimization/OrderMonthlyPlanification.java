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
public class OrderMonthlyPlanification {

    private UUID orderMonthlyPlanificationId;
    private UUID monthlyPlanificationId;

    private UUID unrestrictedDemandId;

    private UUID sizeId;

    private BigDecimal kiloWfe;

    private LocalDate period;
}
