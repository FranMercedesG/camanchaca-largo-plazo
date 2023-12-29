package cl.camanchaca.domain.models.capacity.minimum;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MinimumPeriodDailyProductiveCapacity {
    private UUID id;
    private BigDecimal maximum;
    private LocalDate period;
    private UUID productiveCapacityId;
}
