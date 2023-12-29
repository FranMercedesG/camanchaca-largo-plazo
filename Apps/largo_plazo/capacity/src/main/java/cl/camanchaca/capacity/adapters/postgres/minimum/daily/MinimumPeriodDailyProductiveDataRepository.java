package cl.camanchaca.capacity.adapters.postgres.minimum.daily;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
@Repository
public interface MinimumPeriodDailyProductiveDataRepository extends ReactiveCrudRepository<MinimumPeriodDailyProductiveCapacityData, UUID> {
    @Query("DELETE FROM minimum_daily_period pc where pc.minimum_daily_period_id IN (:collect)")
    Mono<Void> deleteAllByIdIn(List<UUID> collect);
}
