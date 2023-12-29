package cl.camanchaca.parametrization.mappers;

import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import cl.camanchaca.parametrization.adapter.postgresql.product.capaity.ProductCapacityData;

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
