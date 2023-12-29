package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.*;
import cl.camanchaca.business.repositories.ParameterCapacityRepository;
import cl.camanchaca.business.repositories.ProductPlantRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.repositories.ProductiveCapacityRepository;
import cl.camanchaca.domain.dtos.ParameterPlantDTO;
import cl.camanchaca.domain.models.ProductiveCapacity;
import cl.camanchaca.domain.models.parameters.ParameterCapacity;
import cl.camanchaca.domain.models.product.ProductCapacity;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GetParameterCapacityWithPlantUseCase extends Usecase<RequestParams, Mono<ParametersResponseWithColumnNames>> {

    private final ParameterCapacityRepository parameterCapacityRepository;
    private final ProductiveCapacityRepository productiveCapacityRepository;
    private final ProductRepository productRepository;
    private final ProductPlantRepository productPlantRepository;


    @Override
    public Mono<ParametersResponseWithColumnNames> apply(RequestParams params) {
        int offset = (params.getPage() - 1) * params.getSize();
        return getProductCapacity(params, offset)
                .zipWith(productPlantRepository.countByPlantId(params.getPlantId()))
                .map(tuple -> ParametersResponse.of(tuple.getT1(), tuple.getT2()))
                .zipWith(productiveCapacityRepository.getAll().collectList())
                .map(this::getResponse);
    }

    private Mono<List<ProductCapacity>> getProductCapacity(RequestParams params, int offset) {
        return productPlantRepository.getByPlantId(params.getPlantId())
                .filter(ParameterPlantDTO::getStatus)
                .map(ParameterPlantDTO::getProductId)
                .skip(offset)
                .take(params.getSize())
                .flatMap(productCode -> this.getParameterCapacity(productCode, params.getPlantId()), 5)
                .collectList();
    }


    private ParametersResponseWithColumnNames getResponse(Tuple2<ParametersResponse, List<ProductiveCapacity>> tuple) {
        return ParametersResponseWithColumnNames.of(
                tuple.getT1().getData(),
                tuple.getT2()
                        .stream().map(capacity -> Column.of(
                                capacity.getName(),
                                capacity.getId().toString()
                        ))
                        .collect(Collectors.toList()),
                tuple.getT1().getTotal()
        );
    }

    private Mono<ProductCapacity> getParameterCapacity(Integer productCode, UUID plantId) {
        return productRepository.findById(productCode)
                .flatMap(product -> parameterCapacityRepository.getByPlantIdAndProductId(plantId, productCode)
                        .collectList()
                        .map(capacitiesDTOS -> ProductCapacity.builder()
                                .productId(product.getCodigo())
                                .species(product.getEspecie())
                                .capacities(capacitiesDTOS.stream().map(dto ->
                                                ParameterCapacity.builder()
                                                        .id(dto.getId())
                                                        .columnId(dto.getProductiveCapacityId())
                                                        .status(dto.getStatus())
                                                        .build())
                                        .collect(Collectors.toList()))
                                .build())
                );
    }

}
