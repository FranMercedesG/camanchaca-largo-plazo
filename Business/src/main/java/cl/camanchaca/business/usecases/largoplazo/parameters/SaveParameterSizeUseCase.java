package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ParameterSizeRepository;
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

    @Override
    public Flux<ParameterSizePerformanceDTO> apply(List<ParameterSizePerformanceDTO> productSizes) {
        return Flux.fromIterable(productSizes)
                .flatMap(dto -> parameterSizeRepository
                        .getByProductIdAndSizeId(
                                dto.getProductId(),
                                dto.getSizeId()
                        )
                        .next()
                        .map(save -> save.toBuilder()
                                .status(dto.getStatus())
                                .build())
                        .switchIfEmpty(Mono.just(dto)), 5)
                .flatMap(parameterSizeRepository::save)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error con los datos de guardado"));
                });

    }

}
