package cl.camanchaca.parametrization.mappers;

import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.Unit;
import cl.camanchaca.parametrization.adapter.postgresql.size.SizeData;

public class SizeMapper {

    public static Size toSize(SizeData sizeData){
        return Size.builder()
                .id(sizeData.getSizeId())
                .initialRange(sizeData.getMinRange())
                .finalRange(sizeData.getMaxRange())
                .unit(Unit.fromString(sizeData.getUnit()))
                .build();
    }


    private SizeMapper(){}
}
