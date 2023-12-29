package cl.camanchaca.domain.models.optimization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MonthlyPlanification {

    private UUID monthlyPlanificationId;

    private LocalDate initialPeriod;

    private LocalDate finalPeriod;

    private LocalDate creationDate;

    private Integer version;

    private Boolean active;
}
