package cl.camanchaca.parametrization.mappers;

import cl.camanchaca.domain.dtos.ParameterPlantDTO;
import cl.camanchaca.parametrization.adapter.postgresql.product.plant.ProductPlantData;

public class ProductPlantMapper {


    public static ParameterPlantDTO toProductPlant(ProductPlantData data){
        return ParameterPlantDTO.builder()
                .id(data.getProductPlantId())
                .plantId(data.getPlantId())
                .productId(data.getProductId())
                .status(data.getStatus())
                .build();
    }

    public static ProductPlantData toProductPlantData(ParameterPlantDTO data){
        return ProductPlantData.builder()
                .productPlantId(data.getId())
                .plantId(data.getPlantId())
                .productId(data.getProductId())
                .status(data.getStatus())
                .build();
    }


    private ProductPlantMapper(){}

}
