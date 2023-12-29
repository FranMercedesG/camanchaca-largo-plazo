package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ParameterPerformanceRepository;
import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class SaveParameterPerformanceUseCase extends Usecase<List<ParameterSizePerformanceDTO>, Flux<ParameterSizePerformanceDTO>> {

    private final ParameterPerformanceRepository parameterPerformanceRepository;
    @Override
    public Flux<ParameterSizePerformanceDTO> apply(List<ParameterSizePerformanceDTO> performanceDTOS) {
        return Flux.fromIterable(performanceDTOS)
                .flatMap(dto -> parameterPerformanceRepository
                        .getByProductIdAndSizeId(
                                dto.getProductId(),
                                dto.getSizeId()
                        )
                        .next()
                        .map(save -> save.toBuilder()
                                .performance(dto.getPerformance())
                                .build())
                        .switchIfEmpty(Mono.just(dto)), 5)
                .flatMap(parameterPerformanceRepository::save)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error con los datos de guardado"));
                });
    }
}
