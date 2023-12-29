package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.*;
import cl.camanchaca.business.repositories.ParameterCapacityRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.repositories.ProductiveCapacityRepository;
import cl.camanchaca.domain.models.parameters.ParameterCapacity;
import cl.camanchaca.domain.models.product.ProductCapacity;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GetParameterCapacityByCodeUseCase extends Usecase<Integer, Mono<ParametersResponseWithColumnNames>> {
    private final ParameterCapacityRepository parameterCapacityRepository;
    private final ProductRepository productRepository;
    private final ProductiveCapacityRepository productiveCapacityRepository;

    @Override
    public Mono<ParametersResponseWithColumnNames> apply(Integer code) {
        return productRepository.findById(code)
                .flatMap(product -> parameterCapacityRepository.getByProductCode(product.getCodigo())
                        .collectList()
                        .map(parameterSizePerformanceDTOS -> ProductCapacity.builder()
                                .productId(product.getCodigo())
                                .description(product.getDescripcion())
                                .species(product.getEspecie())
                                .capacities(parameterSizePerformanceDTOS.stream()
                                        .map(dto -> ParameterCapacity.builder()
                                                .id(dto.getId())
                                                .columnId(dto.getProductiveCapacityId())
                                                .status(dto.getStatus())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                )
                .zipWith(productRepository.count())
                .map(tuple2 -> ParametersResponse.of(List.of(tuple2.getT1()), tuple2.getT2()))
                .zipWith(productiveCapacityRepository.getAll().collectList())
                .map(tuple -> ParametersResponseWithColumnNames.of(
                                tuple.getT1().getData(),
                                tuple.getT2()
                                        .stream().map(size -> Column.of(size.getName(), size.getId().toString()))
                                        .collect(Collectors.toList()),
                                tuple.getT1().getTotal()
                        )
                );
    }
}
