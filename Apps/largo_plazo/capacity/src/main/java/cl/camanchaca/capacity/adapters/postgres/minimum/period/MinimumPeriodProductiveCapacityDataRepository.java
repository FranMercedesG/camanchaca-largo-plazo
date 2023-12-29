package cl.camanchaca.capacity.adapters.postgres.minimum.period;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface MinimumPeriodProductiveCapacityDataRepository extends ReactiveCrudRepository<MinimumPeriodProductiveCapacityData, UUID> {
}
