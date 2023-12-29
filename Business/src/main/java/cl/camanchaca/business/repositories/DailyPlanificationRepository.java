package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.PlanificationPeriod;
import reactor.core.publisher.Mono;

public interface DailyPlanificationRepository {
    Mono<PlanificationPeriod> saveTurn(PlanificationPeriod turn);
    Mono<PlanificationPeriod> getSelectPeriod();
    Mono<Period> getSelectBetweenPeriod();
}
