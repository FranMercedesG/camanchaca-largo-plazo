package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.*;
import cl.camanchaca.business.repositories.ParameterSizeRepository;
import cl.camanchaca.business.repositories.ProductPlantRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.domain.dtos.ParameterPlantDTO;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.parameters.ParameterSize;
import cl.camanchaca.domain.models.product.ProductSize;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GetParameterSizeWithPlantUseCase extends Usecase<RequestParams, Mono<ParametersResponseWithColumnNames>> {

    private final ParameterSizeRepository parameterSizeRepository;
    private final SizeRepository sizeRepository;
    private final ProductRepository productRepository;
    private final ProductPlantRepository productPlantRepository;

    @Override
    public Mono<ParametersResponseWithColumnNames> apply(RequestParams params) {
        int offset = (params.getPage() - 1) * params.getSize();
        return getProductSize(params, offset)
                .zipWith(productPlantRepository.countByPlantId(params.getPlantId()))
                .map(tuple -> ParametersResponse.of(tuple.getT1(), tuple.getT2()))
                .zipWith(sizeRepository.getAll().collectList())
                .map(this::getResponse);
    }

    private Mono<List<ProductSize>> getProductSize(RequestParams params, int offset) {
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

    private Mono<ProductSize> getParameterSize(Integer productCode, UUID plantId) {
        return productRepository.findById(productCode)
                .flatMap(product ->
                        parameterSizeRepository.getByPlantIdAndProductId(plantId, productCode)
                                .collectList()
                                .map(performanceDTOS -> ProductSize.builder()
                                        .productId(product.getCodigo())
                                        .species(product.getEspecie())
                                        .sizes(performanceDTOS.stream().map(dto ->
                                                        ParameterSize.builder()
                                                                .id(dto.getId())
                                                                .columnId(dto.getSizeId())
                                                                .status(dto.getStatus())
                                                                .build())
                                                .collect(Collectors.toList()))
                                        .build())
                );
    }

}
