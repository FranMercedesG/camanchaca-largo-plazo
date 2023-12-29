package cl.camanchaca.biomass.mappers;

import cl.camanchaca.biomass.adapter.postgres.product.capaity.ProductCapacityData;
import cl.camanchaca.domain.dtos.ParameterCapacityDTO;

public class ProductCapacityMapper {

    public static ParameterCapacityDTO toCapacity(ProductCapacityData productCapacityData){
        return ParameterCapacityDTO.builder()
                .id(productCapacityData.getProductCapacityId())
                .productId(productCapacityData.getProductId())
                .productiveCapacityId(productCapacityData.getProductiveCapacityId())
                .status(productCapacityData.getStatus())
                .build();
    }

    public static ProductCapacityData toProductCapacityData(ParameterCapacityDTO productParameterCapacity){
       return ProductCapacityData.builder()
               .productCapacityId(productParameterCapacity.getId())
                .productId(productParameterCapacity.getProductId())
                .productiveCapacityId(productParameterCapacity.getProductiveCapacityId())
                .status(productParameterCapacity.getStatus())
               .build();
    }

    private ProductCapacityMapper(){

    }

}
