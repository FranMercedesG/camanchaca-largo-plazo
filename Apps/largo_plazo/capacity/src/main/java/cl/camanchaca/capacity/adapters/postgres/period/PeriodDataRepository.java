package cl.camanchaca.capacity.adapters.postgres.period;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PeriodDataRepository extends ReactiveCrudRepository<PeriodData, UUID> {
    Mono<Void> deleteAllByUser(String usuario);

    Flux<PeriodData> findAllByUser(String user);
}
