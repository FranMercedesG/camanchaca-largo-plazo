package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.*;
import cl.camanchaca.business.repositories.GroupRepository;
import cl.camanchaca.business.repositories.ParameterGroupRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.domain.models.parameters.ParameterGroup;
import cl.camanchaca.domain.models.product.ProductGroup;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GetParameterGroupByCodeUseCase extends Usecase<Integer, Mono<ParametersResponseWithColumnNames>> {
    private final ProductRepository productRepository;
    private final ParameterGroupRepository parameterGroupRepository;
    private final GroupRepository groupRepository;

    @Override
    public Mono<ParametersResponseWithColumnNames> apply(Integer code) {
        return productRepository.findById(code)
                .flatMap(product -> parameterGroupRepository.getByProductCode(product.getCodigo())
                        .collectList()
                        .map(parameterSizePerformanceDTOS -> ProductGroup.builder()
                                .productId(product.getCodigo())
                                .description(product.getDescripcion())
                                .species(product.getEspecie())
                                .groups(parameterSizePerformanceDTOS.stream()
                                        .map(dto -> ParameterGroup.builder()
                                                .id(dto.getId())
                                                .columnId(dto.getGroupId())
                                                .status(dto.getStatus())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build())
                )
                .zipWith(productRepository.count())
                .map(tuple2 -> ParametersResponse.of(List.of(tuple2.getT1()), tuple2.getT2()))
                .zipWith(groupRepository.getAll().collectList())
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
