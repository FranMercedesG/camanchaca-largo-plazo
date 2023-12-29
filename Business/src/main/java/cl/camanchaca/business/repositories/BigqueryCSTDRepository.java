package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BigqueryCSTDRepository {

    Mono<Long> count() throws InterruptedException;
    Flux<Product> getAll() throws InterruptedException;

}
