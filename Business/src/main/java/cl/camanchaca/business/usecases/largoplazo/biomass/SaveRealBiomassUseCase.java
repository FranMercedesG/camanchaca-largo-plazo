package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.biomass.RealBiomassRepository;
import cl.camanchaca.domain.models.biomass.RealBiomass;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class SaveRealBiomassUseCase extends Usecase<List<RealBiomass>, Flux<RealBiomass>> {

    private final RealBiomassRepository realBiomassRepository;

    @Override
    public Flux<RealBiomass> apply(List<RealBiomass> realBiomasses) {
        return realBiomassRepository
                .saveAll(realBiomasses)
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error buscando los datos"));
                });
    }
}