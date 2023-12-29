package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.*;
import cl.camanchaca.business.repositories.ParameterSizeRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.domain.models.parameters.ParameterPerformance;
import cl.camanchaca.domain.models.product.ProductPerformance;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@AllArgsConstructor
public class GetParameterPerformanceUseCase extends Usecase<RequestParams, Mono<ParametersResponseWithColumnNames>> {
    private final ProductRepository productRepository;
    private final ParameterSizeRepository parameterSizeRepository;
    private final SizeRepository sizeRepository;

    @Override
    public Mono<ParametersResponseWithColumnNames> apply(RequestParams requestParams) {
        return productRepository.getByPageAndSize(requestParams.getPage(), requestParams.getSize())
                .flatMap(product -> parameterSizeRepository.getByProductCode(product.getCodigo())
                        .collectList()
                        .map(parameterSizePerformanceDTOS -> ProductPerformance.builder()
                                .productId(product.getCodigo())
                                .description(product.getDescripcion())
                                .species(product.getEspecie())
                                .performances(parameterSizePerformanceDTOS.stream()
                                        .map(dto -> ParameterPerformance.builder()
                                                .id(dto.getId())
                                                .columnId(dto.getSizeId())
                                                .performance(dto.getPerformance())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build()), 5
                )
                .collectList()
                .zipWith(productRepository.count())
                .map(tuple2 -> ParametersResponse.of(tuple2.getT1(), tuple2.getT2()))
                .zipWith(sizeRepository.getAll().collectList())
                .map(tuple -> ParametersResponseWithColumnNames.of(
                                tuple.getT1().getData(),
                                tuple.getT2()
                                        .stream().map(size -> Column.of(size.nameColunm(), size.getId().toString()))
                                        .collect(Collectors.toList()),
                                tuple.getT1().getTotal()
                        )
                );

    }
}
