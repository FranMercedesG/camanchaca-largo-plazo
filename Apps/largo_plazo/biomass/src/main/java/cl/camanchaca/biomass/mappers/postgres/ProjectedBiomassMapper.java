package cl.camanchaca.biomass.mappers.postgres;

import cl.camanchaca.biomass.adapter.postgres.biomass.projected.ProjectedBiomassData;
import cl.camanchaca.domain.models.biomass.ProjectedBiomass;

public class ProjectedBiomassMapper {

    public static ProjectedBiomass toProjectedBiomass(ProjectedBiomassData data) {
        return ProjectedBiomass.builder()
                .id(data.getProjectedBiomassId())
                .sizeId(data.getSizeId())
                .pieces(data.getPieces())
                .avgWeight(data.getAvgWeight())
                .kilosWFE(data.getKilosWFE())
                .kilosWFEScenario(data.getKilosWFEScenario())
                .projectionDate(data.getProjectionDate())
                .quality(data.getQuality())
                .build();
    }

    public static ProjectedBiomassData toProjectedBiomassData(ProjectedBiomass data){
        return ProjectedBiomassData.builder()
                .projectedBiomassId(data.getId())
                .sizeId(data.getSizeId())
                .pieces(data.getPieces())
                .avgWeight(data.getAvgWeight())
                .kilosWFE(data.getKilosWFE())
                .kilosWFEScenario(data.getKilosWFEScenario())
                .projectionDate(data.getProjectionDate())
                .quality(data.getQuality())
                .build();
    }

    private ProjectedBiomassMapper(){}

}
