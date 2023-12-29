package cl.camanchaca.biomass.mappers.postgres;

import cl.camanchaca.biomass.adapter.postgres.size.SizeData;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.Unit;

public class SizeMapper {

    public static Size toSize(SizeData sizeData){
        return Size.builder()
                .id(sizeData.getSizeId())
                .initialRange(sizeData.getMinRange())
                .finalRange(sizeData.getMaxRange())
                .unit(Unit.fromString(sizeData.getUnit()))
                .pieceType(sizeData.getPieceType())
                .species(sizeData.getSpecie())
                .build();
    }
    public static SizeData toSizeData(Size size){
        return SizeData.builder()
                .sizeId(size.getId())
                .minRange(size.getInitialRange())
                .maxRange(size.getFinalRange())
                .unit(size.getUnit().toString())
                .pieceType(size.getPieceType())
                .specie(size.getSpecies())
                .build();
    }

    private SizeMapper(){}
}
