package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.*;
import cl.camanchaca.business.repositories.ParameterCapacityRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.repositories.ProductiveCapacityRepository;
import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import cl.camanchaca.domain.models.parameters.ParameterCapacity;
import cl.camanchaca.domain.models.parameters.ParameterSize;
import cl.camanchaca.domain.models.product.ProductCapacity;
import cl.camanchaca.domain.models.product.ProductSize;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@AllArgsConstructor
public class GetParameterCapacityUseCase extends Usecase<RequestParams, Mono<ParametersResponseWithColumnNames>> {

    private final ParameterCapacityRepository parameterCapacityRepository;
    private final ProductRepository productRepository;
    private final ProductiveCapacityRepository productiveCapacityRepository;

    @Override
    public Mono<ParametersResponseWithColumnNames> apply(RequestParams params) {

        return productRepository.getByPageAndSize(params.getPage(), params.getSize())
                .flatMap(product -> parameterCapacityRepository.getByProductCode(product.getCodigo())
                        .collectList()
                        .map(parameterCapacityDTOS -> ProductCapacity.builder()
                                .productId(product.getCodigo())
                                .description(product.getDescripcion())
                                .species(product.getEspecie())
                                .capacities(parameterCapacityDTOS.stream()
                                        .map(dto -> ParameterCapacity.builder()
                                                .id(dto.getId())
                                                .columnId(dto.getProductiveCapacityId())
                                                .status(dto.getStatus())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build()), 5
                )
                .collectList()
                .zipWith(productRepository.count())
                .map(tuple2 -> ParametersResponse.of(tuple2.getT1(), tuple2.getT2()))
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
