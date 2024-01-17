package cl.camanchaca.capacity.mappers;

import cl.camanchaca.capacity.adapters.postgres.maximum.daily.MaximumPeriodDailyProductiveCapacityData;
import cl.camanchaca.domain.models.capacity.maximum.MaximumPeriodDailyProductiveCapacity;

public class PeriodDailyProductiveCapacityMapper {
    public static MaximumPeriodDailyProductiveCapacity toPeriodDailyProductiveCapacity(MaximumPeriodDailyProductiveCapacityData data) {
        return MaximumPeriodDailyProductiveCapacity.builder()
                .maximum(data.getMaximum())
                .period(data.getPeriod())
                .productiveCapacityId(data.getProductiveCapacityId())
                .build();
    }

    public static MaximumPeriodDailyProductiveCapacityData toPeriodDailyProductiveCapacityData(MaximumPeriodDailyProductiveCapacity data) {
        return MaximumPeriodDailyProductiveCapacityData.builder()
                .maximum(data.getMaximum())
                .period(data.getPeriod())
                .productiveCapacityId(data.getProductiveCapacityId())
                .build();
    }

    private PeriodDailyProductiveCapacityMapper(){}
}
