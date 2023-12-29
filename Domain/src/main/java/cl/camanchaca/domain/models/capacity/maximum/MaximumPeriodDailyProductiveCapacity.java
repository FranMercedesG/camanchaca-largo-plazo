package cl.camanchaca.domain.models.capacity.maximum;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaximumPeriodDailyProductiveCapacity {
    private UUID id;
    private BigDecimal maximum;
    private LocalDate period;
    private UUID productiveCapacityId;
}
