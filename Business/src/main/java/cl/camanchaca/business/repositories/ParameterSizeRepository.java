package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ParameterSizeRepository {
    Flux<ParameterSizePerformanceDTO> getAll();

    Mono<ParameterSizePerformanceDTO> getById(UUID id);
    Flux<ParameterSizePerformanceDTO> getByPlantIdAndProductId(UUID plantId, Integer productId);
    Flux<ParameterSizePerformanceDTO> getByPlantIdAndProductIdAndSizeId(UUID plantId, Integer productId, UUID sizeId);
    Flux<ParameterSizePerformanceDTO> saveAll(Flux<ParameterSizePerformanceDTO> performances);
    Mono<ParameterSizePerformanceDTO> save(ParameterSizePerformanceDTO performances);

    Flux<ParameterSizePerformanceDTO> getByPageAndSizeAndPlantId(Integer page, Integer size, UUID plantid);
    Flux<ParameterSizePerformanceDTO> getByProductIdAndSizeId(Integer productId, UUID sizeId);

    Flux<ParameterSizePerformanceDTO> getAllByProductIdIn(Flux<Integer> ids);
    Mono<Long> count();
    Mono<Long> countByPlant(UUID plantId);

    Flux<ParameterSizePerformanceDTO> getByProductCode(Integer codigo);
}
