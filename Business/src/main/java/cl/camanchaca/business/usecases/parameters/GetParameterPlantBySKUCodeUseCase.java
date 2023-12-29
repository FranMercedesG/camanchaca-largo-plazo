package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.*;
import cl.camanchaca.business.repositories.PlantRepository;
import cl.camanchaca.business.repositories.ProductPlantRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.domain.models.parameters.ParameterPlant;
import cl.camanchaca.domain.models.product.ProductPlant;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class GetParameterPlantBySKUCodeUseCase extends Usecase<Integer, Mono<ParametersResponseWithColumnNames>> {

    private final ProductPlantRepository productPlantRepository;
    private final PlantRepository plantRepository;
    private final ProductRepository productRepository;

    @Override
    public Mono<ParametersResponseWithColumnNames> apply(Integer code) {
        return productRepository.findById(code)
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
                                .build()))
                .zipWith(plantRepository.getAll()
                        .map(plant -> Column.of(
                                plant.getName(),
                                plant.getId().toString())
                        )
                        .collectList()
                )
                .map(tuple -> ParametersResponseWithColumnNames.of(
                        List.of(tuple.getT1()),
                        tuple.getT2(),
                        1l
                ));
    }
}
