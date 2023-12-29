package cl.camanchaca.optimization.adapter.postgres.monthly;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.UUID;
@Repository
public interface MonthlyPlanificationDataRepository extends ReactiveCrudRepository<MonthlyPlanificationData, UUID> {

    Flux<MonthlyPlanificationData> findAllByInitialPeriodAndFinalPeriod(LocalDate initPeriod, LocalDate finalPeriod);

}
