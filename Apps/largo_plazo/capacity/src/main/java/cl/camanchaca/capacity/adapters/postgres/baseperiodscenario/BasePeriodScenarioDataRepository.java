package cl.camanchaca.capacity.adapters.postgres.baseperiodscenario;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface BasePeriodScenarioDataRepository extends ReactiveCrudRepository<BasePeriodScenarioData, UUID> {

    @Query("SELECT * FROM base_period_scenario WHERE period BETWEEN :startDate AND :endDate OFFSET :offset LIMIT :size")
    Flux<BasePeriodScenarioData> findAllByDateRangeAndOffsetAndSize(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("offset") Integer offset, @Param("size") Integer size);

    @Query("SELECT * FROM base_period_scenario WHERE period BETWEEN :startDate AND :endDate")
    Flux<BasePeriodScenarioData> findAllByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
