package cl.camanchaca.biomass.adapter.postgres.biomass.projected;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProjectedBiomassDataRepository extends ReactiveCrudRepository<ProjectedBiomassData, UUID> {

    Mono<Void> deleteAllBySizeId(UUID sizeId);
    Flux<ProjectedBiomassData> findAllBySizeId(UUID sizeId);

}
