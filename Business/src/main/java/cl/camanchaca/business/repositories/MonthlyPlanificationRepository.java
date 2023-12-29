package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.optimization.MonthlyPlanification;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

public interface MonthlyPlanificationRepository {
    Flux<MonthlyPlanification> findByInitialAndFinalPeriod(LocalDate initialPeriod, LocalDate finalPeriod);
    Flux<MonthlyPlanification> getByInitPeriodAndFinalPeriod(
            LocalDate initPeriod,
            LocalDate finalPeriod);

    Mono<MonthlyPlanification> getById(UUID id);

    Mono<MonthlyPlanification> save(MonthlyPlanification data);
}
