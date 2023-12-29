package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.*;
import cl.camanchaca.business.repositories.*;
import cl.camanchaca.domain.dtos.ParameterPlantDTO;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.parameters.ParameterPerformance;
import cl.camanchaca.domain.models.product.ProductPerformance;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class GetParameterPerformanceWithPlantUseCase extends Usecase<RequestParams, Mono<ParametersResponseWithColumnNames>> {

    private final ParameterPerformanceRepository parameterPerformanceRepository;
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ProductPlantRepository productPlantRepository;

    @Override
    public Mono<ParametersResponseWithColumnNames> apply(RequestParams params) {
        int offset = (params.getPage() - 1) * params.getSize();
        return getProductPerformance(params, offset)
                .zipWith(productPlantRepository.countByPlantId(params.getPlantId()))
                .map(tuple -> ParametersResponse.of(tuple.getT1(), tuple.getT2()))
                .zipWith(sizeRepository.getAll().collectList())
                .map(this::getResponse);
    }

    private Mono<List<ProductPerformance>> getProductPerformance(RequestParams params, int offset) {
        return productPlantRepository.getByPlantId(params.getPlantId())
                .filter(ParameterPlantDTO::getStatus)
                .map(ParameterPlantDTO::getProductId)
                .skip(offset)
                .take(params.getSize())
                .flatMap(productCode -> this.getParameterSize(productCode, params.getPlantId()), 5)
                .collectList();
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

    private Mono<ProductPerformance> getParameterSize(Integer productCode, UUID plantId) {
        return productRepository.findById(productCode)
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
