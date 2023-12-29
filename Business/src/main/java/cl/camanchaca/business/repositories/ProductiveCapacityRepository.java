package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.ProductiveCapacity;
import reactor.core.publisher.Flux;

public interface ProductiveCapacityRepository {

    Flux<ProductiveCapacity> getAll();
}
