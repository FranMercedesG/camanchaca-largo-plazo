package cl.camanchaca.business.repositories.biomass;

import cl.camanchaca.domain.models.biomass.ProjectedBiomass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ProjectedBiomassRepository {

    Mono<ProjectedBiomass> save(ProjectedBiomass projectedBiomass);
    Flux<ProjectedBiomass> getBySizeId(UUID sizeId);
    Mono<Void> deleteAllBySizeId(UUID sizeId);
}
