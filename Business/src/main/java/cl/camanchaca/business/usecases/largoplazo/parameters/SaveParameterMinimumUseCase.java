package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ParameterMinimumRepository;
import cl.camanchaca.domain.dtos.ParameterMinimumDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class SaveParameterMinimumUseCase extends Usecase<List<ParameterMinimumDTO>, Flux<ParameterMinimumDTO>> {

    private final ParameterMinimumRepository minimumRepository;

    @Override
    public Flux<ParameterMinimumDTO> apply(List<ParameterMinimumDTO> productSizes) {
        return Flux.fromIterable(productSizes)
                .flatMap(dto -> minimumRepository
                        .getByProductIdAndSizeId(
                                dto.getProductId(),
                                dto.getMinimumId()
                        )
                        .next()
                        .map(save -> save.toBuilder()
                                .status(dto.getStatus())
                                .build())
                        .switchIfEmpty(Mono.just(dto)), 5)
                .flatMap(minimumRepository::save)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error con los datos de guardado"));
                });

    }

}
