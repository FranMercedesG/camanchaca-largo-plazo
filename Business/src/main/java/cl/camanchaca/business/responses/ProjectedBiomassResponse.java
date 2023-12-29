package cl.camanchaca.business.responses;

import cl.camanchaca.domain.models.biomass.PieceByDay;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProjectedBiomassResponse {

    private String quality;
    private String size;
    private List<PieceByDay> days;
}