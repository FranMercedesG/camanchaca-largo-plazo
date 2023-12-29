package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.GroupRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
@AllArgsConstructor
public class GetGroupUseCase  {

    private final GroupRepository groupRepository;
    public Mono<ParametersResponse> apply(RequestParams params) {
        return groupRepository.getAll()
                .collectList()
                .flatMap(groups -> Mono.just(ParametersResponse.of(groups, Long.valueOf(groups.size()))));
    }

}
