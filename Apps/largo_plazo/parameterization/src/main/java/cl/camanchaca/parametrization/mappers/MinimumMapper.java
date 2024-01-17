package cl.camanchaca.parametrization.mappers;

import cl.camanchaca.domain.models.Minimum;
import cl.camanchaca.parametrization.adapter.postgresql.minimum.MinimumData;

public class MinimumMapper {
    public static Minimum toMinimum(MinimumData data){
        return Minimum.builder()
                .id(data.getMinimumId())
                .name(data.getName())
                .build();
    }

    public static MinimumData toMinimumData(Minimum data){
        return MinimumData.builder()
                .minimumId(data.getId())
                .name(data.getName())
                .build();
    }

    private MinimumMapper(){}
}
