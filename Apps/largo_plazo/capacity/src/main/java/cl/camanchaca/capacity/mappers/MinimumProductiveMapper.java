package cl.camanchaca.capacity.mappers;

import cl.camanchaca.capacity.adapters.postgres.minimum.daily.MinimumPeriodDailyProductiveCapacityData;
import cl.camanchaca.capacity.adapters.postgres.minimum.minimumcapacity.MinimumCapacityDTO;
import cl.camanchaca.capacity.adapters.postgres.minimum.minimumcapacity.MinimumData;
import cl.camanchaca.capacity.adapters.postgres.minimum.period.MinimumPeriodProductiveCapacityData;
import cl.camanchaca.domain.models.Minimum;
import cl.camanchaca.domain.models.capacity.minimum.MinimumDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumPeriodDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumTotalProductiveCapacity;

public class MinimumProductiveMapper {
    public static MinimumDailyProductiveCapacity toDailyProductiveCapacity(MinimumCapacityDTO o) {
        return MinimumDailyProductiveCapacity
                .builder()
                .productiveCapacityId(o.getCapacityid())
                .periodDailyProductiveCapacityId(o.getPerioddailyid())
                .maximumCapacity(o.getMaximum())
                .name(o.getName())
                .period(o.getPeriod())
                .build();
    }

    public static MinimumPeriodProductiveCapacityData toPeriodProductiveCapacity(MinimumTotalProductiveCapacity o){
        return MinimumPeriodProductiveCapacityData
                .builder()
                .productiveCapacityId(o.getProductivecapacityid())
                .period(o.getPeriod())
                .maximum(o.getMaximumcapacity())
                .build();
    }

    public static MinimumTotalProductiveCapacity toPeriodProductiveCapacity(MinimumPeriodProductiveCapacityData o){
        return MinimumTotalProductiveCapacity
                .builder()
                .totalproductivecapacityid(o.getProductiveCapacityId())
                .productivecapacityid(o.getProductiveCapacityId())
                .period(o.getPeriod())
                .maximumcapacity(o.getMaximum())
                .build();
    }

    public static Minimum toPeriodProductiveCapacity(MinimumData o){
        return Minimum
                .builder()
                .id(o.getMinimumId())
                .name(o.getName())
                .build();
    }

    public static MinimumData toPeriodProductiveCapacity(Minimum o){
        return MinimumData
                .builder()
                .minimumId(o.getId())
                .name(o.getName())
                .build();
    }

    public static MinimumPeriodDailyProductiveCapacityData toPeriodDailyData(MinimumPeriodDailyProductiveCapacity data){
        return MinimumPeriodDailyProductiveCapacityData
                .builder()
                .maximum(data.getMaximum())
                .period(data.getPeriod())
                .productiveCapacityId(data.getProductiveCapacityId())
                .build();
    }

    public static MinimumPeriodDailyProductiveCapacity toPeriodDaily(MinimumPeriodDailyProductiveCapacityData data){
        return MinimumPeriodDailyProductiveCapacity
                .builder()
                .id(data.getId())
                .maximum(data.getMaximum())
                .period(data.getPeriod())
                .productiveCapacityId(data.getProductiveCapacityId())
                .build();
    }

    private MinimumProductiveMapper(){}

}
