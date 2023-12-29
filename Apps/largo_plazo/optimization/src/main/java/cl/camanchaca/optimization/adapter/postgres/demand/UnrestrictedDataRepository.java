package cl.camanchaca.optimization.adapter.postgres.demand;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface UnrestrictedDataRepository extends ReactiveCrudRepository<UnrestrictedDemandData, UUID> {

    @Query("SELECT * FROM unrestricted_demand OFFSET :offset LIMIT :size")
    Flux<UnrestrictedDemandData> findAllByOffsetAndSize(@Param("offset") Integer offset, @Param("size") Integer size);

    @Query("SELECT * FROM unrestricted_demand WHERE periodo = :periodo OFFSET :offset LIMIT :size")
    Flux<UnrestrictedDemandData> findAllByPeriodAndOffsetAndSize(@Param("periodo") LocalDate periodo, @Param("offset") Integer offset, @Param("size") Integer size);

    @Query("SELECT * FROM unrestricted_demand WHERE periodo BETWEEN :startDate AND :endDate OFFSET :offset LIMIT :size")
    Flux<UnrestrictedDemandData> findAllByDateRangeAndOffsetAndSize(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("offset") Integer offset, @Param("size") Integer size);

    @Query("SELECT * FROM unrestricted_demand WHERE oficina = :office  and periodo BETWEEN :startDate AND :endDate OFFSET :offset LIMIT :size")
    Flux<UnrestrictedDemandData> findAllByDateRangeOfficeAndOffsetAndSize(LocalDate startDate, LocalDate endDate, Integer offset, Integer size,  String office);

    Mono<Void> deleteAllByUsuario(String user);
}