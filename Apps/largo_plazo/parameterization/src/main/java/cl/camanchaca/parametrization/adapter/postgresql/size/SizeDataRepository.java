package cl.camanchaca.parametrization.adapter.postgresql.size;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SizeDataRepository extends ReactiveCrudRepository<SizeData, UUID> {
}
