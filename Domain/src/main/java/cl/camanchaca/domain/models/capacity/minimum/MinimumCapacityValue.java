package cl.camanchaca.domain.models.capacity.minimum;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MinimumCapacityValue {
    private LocalDate date;
    private BigDecimal value;
}
