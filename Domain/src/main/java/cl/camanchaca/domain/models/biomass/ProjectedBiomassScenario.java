package cl.camanchaca.domain.models.biomass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProjectedBiomassScenario {
    private LocalDate period;
    private Float pieceNumber;
    private Float averageWeight;
    private String scenario;
    private Float kiloWFE;

}
