package cl.camanchaca.optimization.mapper;

import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.Unit;
import cl.camanchaca.optimization.adapter.postgres.size.SizeData;

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
