package cl.camanchaca.parametrization.mappers;

import cl.camanchaca.domain.dtos.ParameterMinimumDTO;
import cl.camanchaca.parametrization.adapter.postgresql.product.minimum.ProductMinimumData;

public class ProductMinimumMapper {
    public static ParameterMinimumDTO toParameterMinimumDTO(ProductMinimumData data){
        return ParameterMinimumDTO.builder()
                .id(data.getId())
                .minimumId(data.getMinimumId())
                .status(data.getStatus())
                .productId(data.getProductId())
                .build();
    }

    public static ProductMinimumData toProductMinimumData(ParameterMinimumDTO data){
        return ProductMinimumData.builder()
                .id(data.getId())
                .productId(data.getProductId())
                .minimumId(data.getMinimumId())
                .status(data.getStatus())
                .build();
    }

    private ProductMinimumMapper(){}
}
