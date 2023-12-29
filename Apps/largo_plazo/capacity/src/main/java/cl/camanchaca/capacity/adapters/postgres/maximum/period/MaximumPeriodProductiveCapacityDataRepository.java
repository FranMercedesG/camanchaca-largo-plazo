package cl.camanchaca.capacity.adapters.postgres.maximum.period;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface MaximumPeriodProductiveCapacityDataRepository extends ReactiveCrudRepository<MaximumPeriodProductiveCapacityData, UUID> {
}
