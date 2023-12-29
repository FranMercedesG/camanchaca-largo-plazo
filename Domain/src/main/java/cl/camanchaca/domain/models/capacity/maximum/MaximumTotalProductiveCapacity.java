package cl.camanchaca.domain.models.capacity.maximum;

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
public class MaximumTotalProductiveCapacity {
    private UUID productivecapacityid;
    private String name;
    private UUID totalproductivecapacityid;
    private BigDecimal maximumcapacity;
    private LocalDate period;
}
