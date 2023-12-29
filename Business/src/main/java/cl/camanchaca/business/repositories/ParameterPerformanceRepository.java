package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import cl.camanchaca.domain.models.parameters.ParameterPerformance;
import cl.camanchaca.domain.models.parameters.ParameterSize;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ParameterPerformanceRepository {

    Flux<ParameterSizePerformanceDTO> getAll();
    Flux<ParameterSizePerformanceDTO> getByPlantIdAndProductId(UUID plantId, Integer productId);
    Flux<ParameterSizePerformanceDTO> getByPlantIdAndProductIdAndSizeId(UUID plantId, Integer productId, UUID sizeId);
    Flux<ParameterSizePerformanceDTO> saveAll(Flux<ParameterPerformance> performances);
    Flux<ParameterSizePerformanceDTO> getByPageAndSizeAndPlantId(Integer page, Integer size, UUID plantId);
    Flux<ParameterSizePerformanceDTO> getByProductIdAndSizeId(Integer productId, UUID sizeId);
    Mono<ParameterSizePerformanceDTO> save(ParameterSizePerformanceDTO parameterSizePerformanceDTO);
    Mono<Long> count();

}
