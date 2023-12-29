package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.PlanificationPeriod;
import reactor.core.publisher.Mono;

public interface WeeklyPlanificationRepository {
    Mono<PlanificationPeriod> saveWeeklyPlanificationPeriod(PlanificationPeriod turn);
    Mono<PlanificationPeriod> getWeeklyPlanificationPeriod();

}
