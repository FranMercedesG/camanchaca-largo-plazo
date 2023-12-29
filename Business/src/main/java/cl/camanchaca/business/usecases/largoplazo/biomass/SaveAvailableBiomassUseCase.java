package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.biomass.AvailableBiomassRepository;
import cl.camanchaca.domain.dtos.biomass.AvailableBiomassDTO;
import cl.camanchaca.domain.models.biomass.AvailableBiomass;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class SaveAvailableBiomassUseCase extends Usecase<AvailableBiomassDTO, Flux<AvailableBiomass>> {

    private final AvailableBiomassRepository availableBiomassRepository;

    @Override
    public Flux<AvailableBiomass> apply(AvailableBiomassDTO availableBiomasses) {

        return Flux.fromIterable(availableBiomasses.getDeletes())
                .concatMap(availableBiomassRepository::deleteById)
                .thenMany(availableBiomassRepository
                        .saveAll(availableBiomasses.getData()))
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error buscando los datos"));
                });
    }
}