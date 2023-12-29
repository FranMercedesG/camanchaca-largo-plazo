package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.*;
import cl.camanchaca.business.repositories.ParameterPerformanceRepository;
import cl.camanchaca.business.repositories.ProductPlantRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.domain.dtos.ParameterPlantDTO;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.parameters.ParameterPerformance;
import cl.camanchaca.domain.models.product.ProductPerformance;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class GetParameterPerformanceWithSKUCodeAndPlantUseCase extends Usecase<Map<String, String>, Mono<ParametersResponseWithColumnNames>> {

    private final ParameterPerformanceRepository parameterPerformanceRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ProductPlantRepository productPlantRepository;

    @Override
    public Mono<ParametersResponseWithColumnNames> apply(Map<String, String> params) {
        return getParameterPerformance(params.get("productCode"), params.get("plantId"))
                .map(data -> ParametersResponse.of(List.of(data), 1l))
                .zipWith(sizeRepository.getAll().collectList())
                .map(this::getResponse);
    }

    private ParametersResponseWithColumnNames getResponse(Tuple2<ParametersResponse, List<Size>> tuple) {
        return ParametersResponseWithColumnNames.of(
                tuple.getT1().getData(),
                tuple.getT2()
                        .stream().map(size -> Column.of(
                                size.nameColunm(),
                                size.getId().toString()
                        ))
                        .collect(Collectors.toList()),
                tuple.getT1().getTotal()
        );
    }

    private Mono<ProductPerformance> getParameterPerformance(String productCodeString, String plantIdString) {
        Integer productCode = Integer.valueOf(productCodeString);
        UUID plantId = UUID.fromString(plantIdString);
        return productPlantRepository.getByProducttIdAndPlantId(productCode, plantId)
                .filter(ParameterPlantDTO::getStatus)
                .map(ParameterPlantDTO::getProductId)
                .next()
                .flatMap(integer -> productRepository.findById(integer))
                .flatMap(product ->
                        parameterPerformanceRepository.getByPlantIdAndProductId(plantId, productCode)
                                .collectList()
                                .map(performanceDTOS -> ProductPerformance.builder()
                                        .productId(product.getCodigo())
                                        .species(product.getEspecie())
                                        .performances(performanceDTOS.stream().map(dto ->
                                                        ParameterPerformance.builder()
                                                                .id(dto.getId())
                                                                .columnId(dto.getSizeId())
                                                                .performance(dto.getPerformance())
                                                                .build())
                                                .collect(Collectors.toList()))
                                        .build())
                );
    }

}
