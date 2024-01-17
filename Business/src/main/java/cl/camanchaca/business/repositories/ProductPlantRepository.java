package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.dtos.ParameterPlantDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProductPlantRepository {


    Flux<ParameterPlantDTO> saveAll(Flux<ParameterPlantDTO> parameters);

    Flux<ParameterPlantDTO> getByPlantId(UUID plantId);
    Flux<ParameterPlantDTO> getByProducttId(Integer productId);
    Flux<ParameterPlantDTO> getByProducttIdAndPlantId(Integer productId, UUID plantId);
    Mono<Long> countByPlantId(UUID plantId);


}
