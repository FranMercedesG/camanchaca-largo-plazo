package cl.camanchaca.business.usecases.planification;

import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.DailyPlanificationRepository;
import cl.camanchaca.domain.models.PlanificationPeriod;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class DailyPlanificationPeriodUseCase extends Usecase<PlanificationPeriod, Mono<PlanificationPeriod>> {
  
  private final DailyPlanificationRepository dailyPlanificationRepository;

  @Override
  public Mono<PlanificationPeriod> apply(PlanificationPeriod t) {
    return dailyPlanificationRepository.saveTurn(t);
  }
}
