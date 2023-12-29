package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.biomass.GroupSizeMaxRepository;
import cl.camanchaca.business.responses.GroupSizeMaxResponse;
import cl.camanchaca.domain.models.biomass.GroupSizeMax;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class SaveGroupMaxUseCase extends Usecase<GroupSizeMaxResponse, Flux<GroupSizeMax>> {

    private final GroupSizeMaxRepository groupSizeMaxRepository;
    @Override
    public Flux<GroupSizeMax> apply(GroupSizeMaxResponse groupSizeMax) {
        return Flux.fromIterable(groupSizeMax.getDeletes())
                .concatMap(groupSizeMaxRepository::deleteById)
                .thenMany(groupSizeMaxRepository
                        .saveAll(groupSizeMax.getData()))
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error buscando los datos"));
                });
    }
}
