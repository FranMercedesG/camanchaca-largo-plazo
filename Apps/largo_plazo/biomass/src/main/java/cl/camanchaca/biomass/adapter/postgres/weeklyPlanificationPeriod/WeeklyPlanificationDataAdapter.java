package cl.camanchaca.biomass.adapter.postgres.weeklyPlanificationPeriod;

import cl.camanchaca.business.repositories.WeeklyPlanificationRepository;
import cl.camanchaca.domain.models.PlanificationPeriod;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class WeeklyPlanificationDataAdapter implements WeeklyPlanificationRepository {

    private final WeeklyPlanificationDataRepository weeklyPlanificationDataRepository;

    @Override
    public Mono<PlanificationPeriod> saveWeeklyPlanificationPeriod(PlanificationPeriod week) {
        return weeklyPlanificationDataRepository
                .deleteAll()
                .then(weeklyPlanificationDataRepository
                        .save(
                                WeeklyPlanificationData.builder()
                                        .week(week.getPlanificationDate())
                                        .build()
                        ))
                .map(weeklyPlanificationData ->
                        PlanificationPeriod.builder()
                                .planificationDate(weeklyPlanificationData.getWeek())
                                .build());
    }

    @Override
    public Mono<PlanificationPeriod> getWeeklyPlanificationPeriod() {
        return weeklyPlanificationDataRepository.findAll()
                .next()
                .map(weeklyPlanificationData -> PlanificationPeriod.builder()
                        .planificationDate(weeklyPlanificationData.getWeek())
                        .build());
    }
}
