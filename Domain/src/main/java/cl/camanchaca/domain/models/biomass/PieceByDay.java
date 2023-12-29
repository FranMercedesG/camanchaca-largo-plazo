package cl.camanchaca.domain.models.biomass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PieceByDay {
    private UUID id;
    private LocalDate period;
    private LocalDate day;
    private Long value;
    private long piecesBySizeValue;
    private BigDecimal kilosWFEValue;
}
