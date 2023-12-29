package cl.camanchaca.parametrization.mappers;

import cl.camanchaca.domain.models.ProductiveCapacity;
import cl.camanchaca.parametrization.adapter.postgresql.procutive.capacity.ProductiveCapacityData;

public class ProductiveCapacityMapper {

    public static ProductiveCapacity toProductiveCapacity(ProductiveCapacityData productCapacityData){
        return ProductiveCapacity.builder()
                .id(productCapacityData.getProductiveCapacityId())
                .name(productCapacityData.getName())
                .description(productCapacityData.getDescription())
                .build();
    }


    private ProductiveCapacityMapper() {
    }
}
