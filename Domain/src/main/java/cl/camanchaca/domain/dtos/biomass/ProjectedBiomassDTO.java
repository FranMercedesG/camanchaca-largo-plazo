package cl.camanchaca.domain.dtos.biomass;

import cl.camanchaca.domain.models.Size;
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
public class ProjectedBiomassDTO {

    private UUID id;
    private Double number;
    private Double totalNumber;
    private BigDecimal kilosWFE;
    private BigDecimal kilosWFEScenario;
    private BigDecimal avgWeight;
    private LocalDate day;
    private Double standardDeviation;
    private Double quality;
    private String specie;
    private Size size;
    private long pieces;
    private BigDecimal totalKilosWFEScenario;



}
