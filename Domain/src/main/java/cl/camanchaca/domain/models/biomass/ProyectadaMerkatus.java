package cl.camanchaca.domain.models.biomass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProyectadaMerkatus {
    private String year;
    private String month;
    private String scenario;
    private float harvestBiomassByMonth;
    private float harvestCount;
    private float harvestBiomassDividedByHarvestCount;
    private String especie;
}
