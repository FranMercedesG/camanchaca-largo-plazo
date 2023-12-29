package cl.camanchaca.capacity.adapters.postgres.maximum.daily;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;


@Repository
public interface MaximumPeriodDailyProductiveCapacityDataRepository extends ReactiveCrudRepository<MaximumPeriodDailyProductiveCapacityData, UUID> {
    @Query("DELETE FROM period_daily_productive_capacity pc where pc.period_daily_productive_capacity_id IN (:collect)")
    Mono<Void> deleteAllByIdIn(List<UUID> collect);
}
