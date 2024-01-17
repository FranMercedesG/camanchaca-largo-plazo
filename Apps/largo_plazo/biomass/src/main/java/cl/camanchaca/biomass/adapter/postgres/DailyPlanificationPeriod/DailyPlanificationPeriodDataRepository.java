package cl.camanchaca.biomass.adapter.postgres.dailyplanificationperiod;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyPlanificationPeriodDataRepository extends ReactiveCrudRepository<DailyPlanificationPeriodData, Long> {

}
