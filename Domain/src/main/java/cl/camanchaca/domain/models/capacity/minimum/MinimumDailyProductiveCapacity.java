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
public class MinimumDailyProductiveCapacity {
    private UUID productiveCapacityId;
    private String name;
    private UUID periodDailyProductiveCapacityId;
    private BigDecimal maximumCapacity;
    private LocalDate period;
}
