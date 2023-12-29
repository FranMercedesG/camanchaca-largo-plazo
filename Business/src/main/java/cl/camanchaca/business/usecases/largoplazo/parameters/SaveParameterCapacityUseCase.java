package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ParameterCapacityRepository;
import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class SaveParameterCapacityUseCase extends Usecase<List<ParameterCapacityDTO>, Flux<ParameterCapacityDTO>> {

    private ParameterCapacityRepository parameterCapacityRepository;

    @Override
    public Flux<ParameterCapacityDTO> apply(List<ParameterCapacityDTO> parameterCapacityDTOS) {
        return Flux.fromIterable(parameterCapacityDTOS)
                .flatMap(dto -> parameterCapacityRepository
                        .getByProductIdAndProductiveCapacityId(
                                dto.getProductId(),
                                dto.getProductiveCapacityId()
                        )
                        .next()
                        .map(save -> save.toBuilder()
                                .status(dto.getStatus())
                                .build())
                        .switchIfEmpty(Mono.just(dto)), 5)
                .flatMap(parameterCapacityRepository::save)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error con los datos de guardado"));
                });
    }
}
