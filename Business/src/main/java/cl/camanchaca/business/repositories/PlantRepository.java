package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.Plant;
import reactor.core.publisher.Flux;

public interface PlantRepository {

    Flux<Plant> getAll();

}
