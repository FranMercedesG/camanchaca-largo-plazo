package cl.camanchaca.domain.models.biomass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RealBiomass {

    private String hu;
    private String plant;
    private String quality;
    private String size;
    private Double pieces;
    private Double weight;
    private String specie;

}
