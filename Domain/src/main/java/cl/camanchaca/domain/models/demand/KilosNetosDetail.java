package cl.camanchaca.domain.models.demand;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KilosNetosDetail {
    private LocalDate date;
    private BigDecimal value;
}
