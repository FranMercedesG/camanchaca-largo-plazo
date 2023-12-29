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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class GetParameterSizeBySKUCodeAndPlantUseCase extends Usecase<Map<String, String>, Mono<ParametersResponseWithColumnNames>> {

    private final ParameterSizeRepository parameterSizeRepository;
    private final SizeRepository sizeRepository;
    private final ProductRepository productRepository;
    private final ProductPlantRepository productPlantRepository;

    @Override
    public Mono<ParametersResponseWithColumnNames> apply(Map<String, String> params) {
        return getParameterSize(params.get("productCode"), params.get("plantId"))
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

    private Mono<ProductSize> getParameterSize(String productCodeString, String plantIdString) {
        Integer productCode = Integer.valueOf(productCodeString);
        UUID plantId = UUID.fromString(plantIdString);
        return productPlantRepository.getByProducttIdAndPlantId(productCode, plantId)
                .filter(ParameterPlantDTO::getStatus)
                .map(ParameterPlantDTO::getProductId)
                .next()
                .flatMap(integer -> productRepository.findById(integer))
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
