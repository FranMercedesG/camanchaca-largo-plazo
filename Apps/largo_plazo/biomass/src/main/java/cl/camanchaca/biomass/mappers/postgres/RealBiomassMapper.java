package cl.camanchaca.biomass.mappers.postgres;

import cl.camanchaca.biomass.adapter.postgres.biomass.real.RealBiomassData;
import cl.camanchaca.domain.models.biomass.RealBiomass;

public class RealBiomassMapper {

    public static RealBiomass toRealBiomass(RealBiomassData realBiomassData){
        return RealBiomass.builder()
                .hu(realBiomassData.getHu())
                .size(realBiomassData.getSize())
                .quality(realBiomassData.getQuality())
                .pieces(realBiomassData.getPieces())
                .weight(realBiomassData.getWeight())
                .specie(realBiomassData.getSpecie())
                .plant(realBiomassData.getPlant())
                .build();
    }

    public static RealBiomassData toRealBiomassData(RealBiomass realBiomass){
        return RealBiomassData.builder()
                .hu(realBiomass.getHu())
                .plant(realBiomass.getPlant())
                .size(realBiomass.getSize())
                .quality(realBiomass.getQuality())
                .pieces(realBiomass.getPieces())
                .weight(realBiomass.getWeight())
                .specie(realBiomass.getSpecie())
                .build();
    }

    private RealBiomassMapper(){}
}
