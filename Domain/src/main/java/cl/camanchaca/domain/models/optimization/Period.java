package cl.camanchaca.domain.models.optimization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Period {
    private UUID demandPeriodId;
    private LocalDate period;
    private Boolean status;
}
