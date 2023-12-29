package cl.camanchaca.parametrization.adapter.postgresql.procutive.capacity;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductiveCapacityDataRepository extends ReactiveCrudRepository<ProductiveCapacityData, UUID> {
}
