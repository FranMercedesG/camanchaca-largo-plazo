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
public class ProjectedBiomass {

    private UUID id;
    private UUID sizeId;
    private long pieces;
    private LocalDate projectionDate;
    private BigDecimal avgWeight;
    private BigDecimal kilosWFE;
    private BigDecimal kilosWFEScenario;
    private BigDecimal quality;

}
