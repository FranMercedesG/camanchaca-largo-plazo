package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.dtos.ParameterGroupDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ParameterGroupRepository {
    Flux<ParameterGroupDTO> getByProductCode(Integer codigo);

    Flux<ParameterGroupDTO> getAll();

    Flux<ParameterGroupDTO> getByProductIdAndGroupId(Integer productId, UUID groudId);
    Mono<ParameterGroupDTO> save(ParameterGroupDTO parameterGroupDTO);

    Mono<Void> deleteById(UUID uuid);
    
    Mono<Void> deleteAll();

    Flux<ParameterGroupDTO> saveAll(List<ParameterGroupDTO> data);

    Flux<ParameterGroupDTO> saveAllParameterAndGroup(List<ParameterGroupDTO> data);

    Mono<ParameterGroupDTO> saveParameterAndGroup(ParameterGroupDTO parameterGroupDTO);

    Mono<Void> deleteGroupById(UUID uuid);


}
