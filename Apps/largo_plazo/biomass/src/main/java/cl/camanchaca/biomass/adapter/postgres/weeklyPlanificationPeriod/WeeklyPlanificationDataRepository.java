package cl.camanchaca.biomass.adapter.postgres.weeklyPlanificationPeriod;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklyPlanificationDataRepository extends ReactiveCrudRepository<WeeklyPlanificationData, Long> {
}
