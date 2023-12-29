package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ParameterSizeRepository;
import cl.camanchaca.business.validations.ValidateByPlant;
import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class SaveParameterSizeUseCase extends Usecase<List<ParameterSizePerformanceDTO>, Flux<ParameterSizePerformanceDTO>> {

    private final ParameterSizeRepository parameterSizeRepository;
    private final ValidateByPlant validateByPlant;

    @Override
    public Flux<ParameterSizePerformanceDTO> apply(List<ParameterSizePerformanceDTO> productSizes) {
        return validateByPlant.availablePlantForSizePerformance(productSizes)
                .flatMap(performanceDTO -> parameterSizeRepository
                        .getByPlantIdAndProductIdAndSizeId(
                                performanceDTO.getPlantId(),
                                performanceDTO.getProductId(),
                                performanceDTO.getSizeId()
                        )
                        .next()
                        .map(performanceBd -> performanceBd.toBuilder()
                                .status(performanceDTO.getStatus())
                                .build())
                        .switchIfEmpty(Mono.just(performanceDTO)), 5)
                .flatMap(parameterSizeRepository::save)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error con los datos de guardado"));
                });

    }

}
