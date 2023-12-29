package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.Column;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.ParametersResponseWithColumnNames;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.*;
import cl.camanchaca.domain.models.parameters.ParameterMinimum;
import cl.camanchaca.domain.models.product.ProductMinimum;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GetParameterMinimunAsignationByCodeUseCase extends Usecase<Integer, Mono<ParametersResponseWithColumnNames>> {

    private final ParameterMinimumRepository parameterMinimumRepository;
    private final ProductRepository productRepository;
    private final MinimumCapacityRepository minimumRepository;
    @Override
    public Mono<ParametersResponseWithColumnNames> apply(Integer code) {
        return productRepository.findById(code)
                .flatMap(product -> parameterMinimumRepository.getByProductCode(product.getCodigo())
                        .collectList()
                        .map(parameterMinimumDTOS -> ProductMinimum.builder()
                                .productId(product.getCodigo())
                                .description(product.getDescripcion())
                                .species(product.getEspecie())
                                .minimums(parameterMinimumDTOS.stream()
                                        .map(dto -> ParameterMinimum.builder()
                                                .id(dto.getId())
                                                .columnId(dto.getMinimumId())
                                                .status(dto.getStatus())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                )
                .zipWith(productRepository.count())
                .map(tuple2 -> ParametersResponse.of(List.of(tuple2.getT1()), tuple2.getT2()))
                .zipWith(minimumRepository.getAll().collectList())
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
