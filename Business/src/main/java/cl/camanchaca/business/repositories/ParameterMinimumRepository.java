package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.dtos.ParameterMinimumDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ParameterMinimumRepository {
    Flux<ParameterMinimumDTO> getByProductCode(Integer codigo);
    Flux<ParameterMinimumDTO> getByProductIdAndSizeId(Integer productId, UUID sizeId);
    Mono<ParameterMinimumDTO> save(ParameterMinimumDTO parameterMinimumDTO);

}
