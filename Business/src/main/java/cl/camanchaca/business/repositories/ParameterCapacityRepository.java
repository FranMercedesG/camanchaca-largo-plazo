package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ParameterCapacityRepository {

    Flux<ParameterCapacityDTO> getAll();

    Flux<ParameterCapacityDTO> getByPlantIdAndProductId(UUID plantId, Integer productId);
    Flux<ParameterCapacityDTO> getByPlantIdAndProductIdAndProductiveCapacityIdId(UUID plantId, Integer productId, UUID productiveCapacityId);
    Flux<ParameterCapacityDTO> saveAll(Flux<ParameterCapacityDTO> capacities);
    Flux<ParameterCapacityDTO> getByPageAndSizeAndPlantId(Integer page, Integer size, UUID plantId);
    Flux<ParameterCapacityDTO> getByPageAndSize(Integer page, Integer size);
    Mono<Long> count();
    Mono<Long> countByPlantId(UUID plantId);

    Mono<ParameterCapacityDTO> save(ParameterCapacityDTO parameterCapacityDTO);

    Flux<ParameterCapacityDTO> getByProductIdAndProductiveCapacityId(Integer productId, UUID productiveCapacityId);

    Flux<ParameterCapacityDTO> getByProductCode(Integer codigo);
}
