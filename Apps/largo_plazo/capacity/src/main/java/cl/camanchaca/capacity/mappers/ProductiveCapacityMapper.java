package cl.camanchaca.capacity.mappers;

import cl.camanchaca.capacity.adapters.postgres.maximum.period.MaximumPeriodProductiveCapacityData;
import cl.camanchaca.capacity.adapters.postgres.maximum.productivecapacity.MaximumProductiveCapacityDTO;
import cl.camanchaca.capacity.adapters.postgres.maximum.productivecapacity.MaximumProductiveCapacityData;
import cl.camanchaca.domain.models.ProductiveCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumTotalProductiveCapacity;

public class ProductiveCapacityMapper {

    public static MaximumDailyProductiveCapacity toDailyProductiveCapacity(MaximumProductiveCapacityDTO data) {
        return MaximumDailyProductiveCapacity.builder()
                .productiveCapacityId(data.getCapacityid())
                .periodDailyProductiveCapacityId(data.getPerioddailyid())
                .maximumCapacity(data.getMaximum())
                .name(data.getName())
                .period(data.getPeriod())
                .build();
    }

    public static MaximumProductiveCapacityDTO toProductiveCapacityDTO(MaximumDailyProductiveCapacity data) {
        return MaximumProductiveCapacityDTO.builder()
                .capacityid(data.getProductiveCapacityId())
                .perioddailyid(data.getPeriodDailyProductiveCapacityId())
                .maximum(data.getMaximumCapacity())
                .period(data.getPeriod())
                .name(data.getName())
                .build();
    }

    public static ProductiveCapacity toProductiveCapacity(MaximumProductiveCapacityData o){
        return ProductiveCapacity
                .builder()
                .description(o.getName())
                .name(o.getName())
                .id(o.getId())
                .build();
    }

    public static MaximumPeriodProductiveCapacityData toPeriodCapacityData(MaximumTotalProductiveCapacity o){
        return MaximumPeriodProductiveCapacityData
                        .builder()
                        .productiveCapacityId(o.getProductivecapacityid())
                        .period(o.getPeriod())
                        .maximum(o.getMaximumcapacity())
                        .build();
    }

    public static MaximumTotalProductiveCapacity toTotalProductiveCapacity(MaximumPeriodProductiveCapacityData o){
        return MaximumTotalProductiveCapacity
                .builder()
                .totalproductivecapacityid(o.getProductiveCapacityId())
                .productivecapacityid(o.getProductiveCapacityId())
                .period(o.getPeriod())
                .maximumcapacity(o.getMaximum())
                .build();
    }

}
