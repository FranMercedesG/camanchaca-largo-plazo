package cl.camanchaca.biomass.adapter.postgres.DailyPlanificationPeriod;

import cl.camanchaca.business.repositories.DailyPlanificationRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.PlanificationPeriod;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DailyPlanificationPeriodDataAdapter implements DailyPlanificationRepository {

    private final DailyPlanificationPeriodDataRepository repository;

    public DailyPlanificationPeriodDataAdapter(DailyPlanificationPeriodDataRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<PlanificationPeriod> saveTurn(PlanificationPeriod turn) {
        return repository
                .deleteAll()
                .then(repository.save(DailyPlanificationPeriodData.builder()
                        .day(turn.getPlanificationDate())
                        .build()))
                .map(dailyPlanificationPeriodData -> PlanificationPeriod
                        .builder()
                        .planificationDate(dailyPlanificationPeriodData.getDay())
                        .build()
                );
    }

    @Override
    public Mono<PlanificationPeriod> getSelectPeriod() {
        return repository.findAll()
                .next()
                .map(dailyPlanificationPeriodData -> PlanificationPeriod
                        .builder()
                        .planificationDate(dailyPlanificationPeriodData.getDay())
                        .build()
                );
    }

    @Override
    public Mono<Period> getSelectBetweenPeriod() {
        return null;
    }

}
