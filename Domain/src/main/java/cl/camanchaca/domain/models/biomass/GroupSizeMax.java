package cl.camanchaca.domain.models.biomass;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GroupSizeMax {
    private UUID groupSizeMaxId;
    private UUID groupId;
    private UUID sizeId;
    private LocalDate period;
    private BigDecimal maxLimit;
}
