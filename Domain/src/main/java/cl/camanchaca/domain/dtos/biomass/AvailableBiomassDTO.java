package cl.camanchaca.domain.dtos.biomass;

import cl.camanchaca.domain.models.biomass.AvailableBiomass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AvailableBiomassDTO {

    private List<AvailableBiomass> data;
    private List<String> deletes;

}
