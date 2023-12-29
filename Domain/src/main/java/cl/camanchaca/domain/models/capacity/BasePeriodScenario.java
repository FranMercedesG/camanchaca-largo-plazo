package cl.camanchaca.domain.models.capacity;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasePeriodScenario {
    private LocalDate period;
    private Integer extraDays;
    private Integer workDays;
}
