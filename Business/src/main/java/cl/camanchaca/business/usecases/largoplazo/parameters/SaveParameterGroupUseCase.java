package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ParameterGroupRepository;
import cl.camanchaca.business.repositories.ParameterMinimumRepository;
import cl.camanchaca.domain.dtos.ParameterGroupDTO;
import cl.camanchaca.domain.dtos.ParameterMinimumDTO;
import cl.camanchaca.domain.dtos.group.GroupSKUParameterDTO;
import cl.camanchaca.domain.models.parameters.ParameterGroup;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class SaveParameterGroupUseCase extends Usecase<GroupSKUParameterDTO, Flux<ParameterGroupDTO>> {

    private final ParameterGroupRepository parameterGroupRepository;

    @Override
    public Flux<ParameterGroupDTO> apply(GroupSKUParameterDTO productSKU) {
        return Flux.fromIterable(productSKU.getDeletes())
                .concatMap(parameterGroupRepository::deleteGroupById)
                .thenMany(parameterGroupRepository
                        .saveAllParameterAndGroup(productSKU.getData()))
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error buscando los datos"));
                });

    }

}
