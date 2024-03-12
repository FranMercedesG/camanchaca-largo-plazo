package cl.camanchaca.optimization.adapter.postgres.demandperiod;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Repository
public interface DemandPeriodDataRepository extends ReactiveCrudRepository<DemandPeriodData, UUID> {
    @Query("INSERT INTO demand_period (demand_id, period, status) " +
            "VALUES (:#{#demand.unrestrictedDemandId}, :#{#demand.period}, :#{#demand.status}) " +
            "RETURNING *")
    Mono<DemandPeriodData> insertData(DemandPeriodData demand);

    @Query("SELECT * FROM demand_period WHERE demand_id = :unrestrictedDemandId AND period = :period")
    Mono<DemandPeriodData> findByUnrestrictedDemandIdAndPeriod(UUID unrestrictedDemandId, LocalDate period);

    @Query("SELECT * FROM demand_period WHERE demand_id = :unrestrictedDemandId AND period IN (:periods)")
    Flux<DemandPeriodData> findByUnrestrictedDemandIdAndPeriods(UUID unrestrictedDemandId, List<LocalDate> periods);



}
