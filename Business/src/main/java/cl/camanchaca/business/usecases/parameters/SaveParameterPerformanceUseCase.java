package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ParameterPerformanceRepository;
import cl.camanchaca.business.validations.ValidateByPlant;
import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class SaveParameterPerformanceUseCase extends Usecase<List<ParameterSizePerformanceDTO>, Flux<ParameterSizePerformanceDTO>> {

    private final ValidateByPlant validateByPlant;
    private final ParameterPerformanceRepository parameterPerformanceRepository;
    @Override
    public Flux<ParameterSizePerformanceDTO> apply(List<ParameterSizePerformanceDTO> performanceDTOS) {
        return validateByPlant.availablePlantForSizePerformance(performanceDTOS)
                .flatMap(performanceDTO -> parameterPerformanceRepository
                        .getByPlantIdAndProductIdAndSizeId(
                                performanceDTO.getPlantId(),
                                performanceDTO.getProductId(),
                                performanceDTO.getSizeId()
                        )
                        .next()
                        .map(performanceBd -> performanceBd.toBuilder()
                                .performance(performanceDTO.getPerformance())
                                .build())
                        .switchIfEmpty(Mono.just(performanceDTO)), 5)
                .flatMap(parameterPerformanceRepository::save)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error con los datos de guardado"));
                });


    }
}
