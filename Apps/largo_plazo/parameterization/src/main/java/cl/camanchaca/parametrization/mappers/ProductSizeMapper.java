package cl.camanchaca.parametrization.mappers;

import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import cl.camanchaca.parametrization.adapter.postgresql.product.size.ProductSizeData;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductSizeMapper {

    private ProductSizeMapper() {
    }

    public static ParameterSizePerformanceDTO toParameterSize(ProductSizeData productSizeData) {
        return ParameterSizePerformanceDTO.builder()
                .id(productSizeData.getSizeProductId())
                .sizeId(productSizeData.getSizeId())
                .productId(productSizeData.getProductId())
                .performance(productSizeData.getPerformance())
                .status(defaultStatus(productSizeData.getStatus()))
                .build();

    }

    public static ProductSizeData toParameterSizeData(ParameterSizePerformanceDTO productSize) {
        return ProductSizeData.builder()
                .sizeProductId(productSize.getId())
                .productId(productSize.getProductId())
                .sizeId(productSize.getSizeId())
                .performance(defaultPerformance(productSize.getPerformance()))
                .status(defaultStatus(productSize.getStatus()))
                .build();

    }

    private static Boolean defaultStatus(Boolean status) {
        return Objects.requireNonNullElse(status, false);
    }

    private static BigDecimal defaultPerformance(BigDecimal performance){
        return Objects.requireNonNullElse(performance, BigDecimal.ZERO);
    }

}
