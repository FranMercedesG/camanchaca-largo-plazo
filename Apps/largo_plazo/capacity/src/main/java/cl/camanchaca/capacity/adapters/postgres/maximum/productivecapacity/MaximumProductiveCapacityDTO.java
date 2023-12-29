package cl.camanchaca.capacity.adapters.postgres.maximum.productivecapacity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaximumProductiveCapacityDTO {
    private UUID capacityid;
    private String name;
    private UUID perioddailyid;
    private BigDecimal maximum;
    private LocalDate period;
}
