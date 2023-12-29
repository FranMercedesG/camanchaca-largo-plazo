package cl.camanchaca.biomass.mappers.postgres;

import cl.camanchaca.biomass.adapter.postgres.biomass.available.AvailableBiomassData;
import cl.camanchaca.domain.models.biomass.AvailableBiomass;

public class AvailableBiomassMapper {

    public static AvailableBiomass toAvailableBiomass(AvailableBiomassData availableBiomassData){
        return AvailableBiomass.builder()
                .hu(availableBiomassData.getHu())
                .externalHu(availableBiomassData.getExternalHu())
                .type(availableBiomassData.getType())
                .size(availableBiomassData.getSize())
                .quality(availableBiomassData.getQuality())
                .header(availableBiomassData.getHeader())
                .manufacturingOrder(availableBiomassData.getManufacturingOrder())
                .batch(availableBiomassData.getBatch())
                .folio(availableBiomassData.getFolio())
                .center(availableBiomassData.getCenter())
                .fecpro(availableBiomassData.getFecpro())
                .permanency(availableBiomassData.getPermanency())
                .pieces(availableBiomassData.getPieces())
                .weight(availableBiomassData.getWeight())
                .specie(availableBiomassData.getSpecie())
                .build();
    }

    public static AvailableBiomassData toAvailableBiomassData(AvailableBiomass availableBiomass){
        return AvailableBiomassData.builder()
                .hu(availableBiomass.getHu())
                .externalHu(availableBiomass.getExternalHu())
                .type(availableBiomass.getType())
                .size(availableBiomass.getSize())
                .quality(availableBiomass.getQuality())
                .header(availableBiomass.getHeader())
                .manufacturingOrder(availableBiomass.getManufacturingOrder())
                .batch(availableBiomass.getBatch())
                .folio(availableBiomass.getFolio())
                .center(availableBiomass.getCenter())
                .fecpro(availableBiomass.getFecpro())
                .permanency(availableBiomass.getPermanency())
                .pieces(availableBiomass.getPieces())
                .weight(availableBiomass.getWeight())
                .specie(availableBiomass.getSpecie())
                .build();
    }

    private AvailableBiomassMapper(){}
}
