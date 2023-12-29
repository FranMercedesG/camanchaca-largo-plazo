package cl.camanchaca.business.repositories.bigquery;

import cl.camanchaca.domain.models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CSTDBQRepository {

    Mono<Long> count() throws InterruptedException;
    Flux<Product> getAll() throws InterruptedException;

}
