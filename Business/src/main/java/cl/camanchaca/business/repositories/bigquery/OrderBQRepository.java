package cl.camanchaca.business.repositories.bigquery;

import reactor.core.publisher.Flux;

public interface OrderBQRepository {
    Flux<String> getAllIncoTerms();
    Flux<String> getAllDestiantionPort();
}
