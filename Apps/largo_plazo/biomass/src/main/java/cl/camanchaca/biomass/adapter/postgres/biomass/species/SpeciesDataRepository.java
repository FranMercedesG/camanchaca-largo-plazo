package cl.camanchaca.biomass.adapter.postgres.biomass.species;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;


public interface SpeciesDataRepository extends ReactiveCrudRepository<SpeciesData, String> {

    @Query("SELECT DISTINCT especie FROM products")
    Flux<String> findAllSpecies();

}
