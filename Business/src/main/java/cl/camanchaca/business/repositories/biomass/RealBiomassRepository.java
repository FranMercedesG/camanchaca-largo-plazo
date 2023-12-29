package cl.camanchaca.business.repositories.biomass;

import cl.camanchaca.domain.models.biomass.RealBiomass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface RealBiomassRepository {

    Flux<RealBiomass> getAll();
    Flux<RealBiomass> saveAll(List<RealBiomass> realBiomasses);
    Mono<RealBiomass> save(RealBiomass realBiomass);

    Mono<Void> deleteAll();

    Flux<RealBiomass> getAllByPage(Integer size, Integer offset);

    Mono<Long> count();

}
