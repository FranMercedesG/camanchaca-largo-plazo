package cl.camanchaca.business.repositories.biomass;

import reactor.core.publisher.Flux;

public interface SpeciesRepository {

    Flux<String> getAll();

}
