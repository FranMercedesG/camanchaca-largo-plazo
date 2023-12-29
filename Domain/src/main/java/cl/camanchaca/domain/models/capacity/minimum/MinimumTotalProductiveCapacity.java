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
public class MinimumTotalProductiveCapacity {
    private UUID productivecapacityid;
    private String name;
    private UUID totalproductivecapacityid;
    private BigDecimal maximumcapacity;
    private LocalDate period;
}
