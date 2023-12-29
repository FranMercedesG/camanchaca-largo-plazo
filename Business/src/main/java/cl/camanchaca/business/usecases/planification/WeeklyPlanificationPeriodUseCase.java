package cl.camanchaca.business.usecases.planification;

import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.WeeklyPlanificationRepository;
import cl.camanchaca.domain.models.PlanificationPeriod;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;



@AllArgsConstructor
public class WeeklyPlanificationPeriodUseCase extends Usecase<PlanificationPeriod, Mono<PlanificationPeriod>> {

    private final WeeklyPlanificationRepository weeklyPlanificationRepository;

    @Override
    public Mono<PlanificationPeriod> apply(PlanificationPeriod weeklyPlanification) {
        return weeklyPlanificationRepository
                .saveWeeklyPlanificationPeriod(weeklyPlanification);
    }


}
