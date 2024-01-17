package cl.camanchaca.parametrization.mappers;

import cl.camanchaca.domain.dtos.ParameterGroupDTO;
import cl.camanchaca.parametrization.adapter.postgresql.group.GroupData;
import cl.camanchaca.parametrization.adapter.postgresql.product.group.ProductGroupData;

public class ProductGroupMapper {
    public static ParameterGroupDTO toParameterGroupDTO(ProductGroupData data){
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

    public static GroupData toGroupData(ParameterGroupDTO parameterGroupDTO) {
        return GroupData.builder()
                .groupId(parameterGroupDTO.getGroupId())
                .groupName(parameterGroupDTO.getGroupName())
                .build();
    }
    private ProductGroupMapper(){}
}
