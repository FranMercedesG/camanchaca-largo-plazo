package cl.camanchaca.business.utils;

import cl.camanchaca.domain.models.biomass.ProjectedBiomass;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProjectedBiomassAccumulator {

    private double availablePieces;
    private List<ProjectedBiomass> projectedBiomasses;

}
