package cl.camanchaca.parametrization.adapter.postgresql.minimum;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface MinimumDataRepository  extends ReactiveCrudRepository<MinimumData, UUID> {
}
