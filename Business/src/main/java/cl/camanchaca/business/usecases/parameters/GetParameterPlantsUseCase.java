package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.*;
import cl.camanchaca.business.repositories.PlantRepository;
import cl.camanchaca.business.repositories.ProductPlantRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.domain.models.parameters.ParameterPlant;
import cl.camanchaca.domain.models.product.ProductPlant;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@AllArgsConstructor
public class GetParameterPlantsUseCase extends Usecase<RequestParams, Mono<ParametersResponseWithColumnNames>> {

    private final ProductPlantRepository productPlantRepository;
    private final PlantRepository plantRepository;
    private final ProductRepository productRepository;

    @Override
    public Mono<ParametersResponseWithColumnNames> apply(RequestParams params) {
        return productRepository.getByPageAndSize(params.getPage(), params.getSize())
                .flatMap(product -> productPlantRepository.getByProducttId(product.getCodigo())
                        .map(parameterPlantDTO -> ParameterPlant.builder()
                                .id(parameterPlantDTO.getId())
                                .columnId(parameterPlantDTO.getPlantId())
                                .status(parameterPlantDTO.getStatus())
                                .build())
                        .collectList()
                        .map(parameterPlants -> ProductPlant.builder()
                                .productId(product.getCodigo())
                                .species(product.getEspecie())
                                .plants(parameterPlants)
                                .build()),
                5)
                .collectList()
                .zipWith(productRepository.count())
                .map(tuple -> ParametersResponse.of(tuple.getT1(), tuple.getT2()))
                .zipWith(plantRepository.getAll()
                        .map(plant -> Column.of(
                                plant.getName(),
                                plant.getId().toString())
                        )
                        .collectList()
                )
                .map(tuple -> ParametersResponseWithColumnNames.of(
                        tuple.getT1().getData(),
                        tuple.getT2(),
                        tuple.getT1().getTotal()
                ));
    }
}
