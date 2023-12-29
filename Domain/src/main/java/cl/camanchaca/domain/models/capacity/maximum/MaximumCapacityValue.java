package cl.camanchaca.domain.models.capacity.maximum;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaximumCapacityValue {
    private LocalDate date;
    private BigDecimal value;
}
