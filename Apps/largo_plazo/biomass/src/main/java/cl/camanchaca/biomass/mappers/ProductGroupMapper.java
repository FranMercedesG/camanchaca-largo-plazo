package cl.camanchaca.biomass.mappers;

import cl.camanchaca.biomass.adapter.postgres.product.group.ProductGroupData;
import cl.camanchaca.domain.dtos.ParameterGroupDTO;

public class ProductGroupMapper {
    public static ParameterGroupDTO toParameterGroupDTOp(ProductGroupData data){
        return ParameterGroupDTO.builder()
                .id(data.getId())
                .productId(data.getProductId())
                .groupId(data.getGroupId())
                .status(data.getStatus())
                .build();
    }

    public static ProductGroupData toProductGroupData(ParameterGroupDTO data){
        return ProductGroupData.builder()
                .id(data.getId())
                .productId(data.getProductId())
                .groupId(data.getGroupId())
                .status(data.getStatus())
                .build();
    }
}
