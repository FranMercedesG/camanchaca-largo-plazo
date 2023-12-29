package cl.camanchaca.domain.models.demand;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RmpDetail {
    private LocalDate date;
    private BigDecimal value;
}
