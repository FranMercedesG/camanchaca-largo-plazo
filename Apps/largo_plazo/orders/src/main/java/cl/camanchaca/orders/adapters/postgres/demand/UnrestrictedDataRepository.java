package cl.camanchaca.orders.adapters.postgres.demand;

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

    @Query("SELECT * FROM unrestricted_demand WHERE periodo BETWEEN :startDate AND :endDate ORDER BY updated_at DESC OFFSET :offset LIMIT :size")
    Flux<UnrestrictedDemandData> findAllByDateRangeAndOffsetAndSize(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("offset") Integer offset, @Param("size") Integer size);


    @Query("SELECT * FROM unrestricted_demand WHERE periodo BETWEEN :startDate AND :endDate AND (especie = :especie OR :especie IS NULL) AND (family = :family OR :family IS NULL) ORDER BY updated_at DESC OFFSET :offset LIMIT :size")
    Flux<UnrestrictedDemandData> findAllByDateRangeAndOffsetAndSizeAndFilters(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("offset") Integer offset, @Param("size") Integer size, String especie, String family);
    @Query("SELECT * FROM unrestricted_demand WHERE oficina = :office  and periodo BETWEEN :startDate AND :endDate OFFSET :offset LIMIT :size")
    Flux<UnrestrictedDemandData> findAllByDateRangeOfficeAndOffsetAndSize(LocalDate startDate, LocalDate endDate, Integer offset, Integer size,  String office);

    @Query("SELECT * FROM unrestricted_demand WHERE LOWER(oficina) = LOWER(:office) and periodo BETWEEN :startDate AND :endDate AND (especie = :especie OR :especie IS NULL) AND (family = :family OR :family IS NULL) ORDER BY updated_at DESC OFFSET :offset LIMIT :size")
    Flux<UnrestrictedDemandData> findAllOfficeByDateRangeAndOffsetAndSizeAndFilters(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("offset") Integer offset, @Param("size") Integer size, String especie, String family, String office);

    @Query("SELECT * FROM unrestricted_demand WHERE LOWER(oficina) = LOWER(:office) and periodo BETWEEN :startDate AND :endDate AND (especie = :especie OR :especie IS NULL) AND (family = :family OR :family IS NULL) ORDER BY updated_at DESC")
    Flux<UnrestrictedDemandData> findAllOfficeByDateRangeAndFilters(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, String especie, String family, String office);

    @Query("SELECT * FROM unrestricted_demand WHERE oficina = :office  and periodo BETWEEN :startDate AND :endDate")
    Flux<UnrestrictedDemandData> findAllByDateRangeOffice(LocalDate startDate, LocalDate endDate,  String office);

    @Query("SELECT * FROM unrestricted_demand WHERE periodo BETWEEN :startDate AND :endDate")
    Flux<UnrestrictedDemandData> findAllByDateRange(LocalDate startDate, LocalDate endDate);

    Mono<Void> deleteAllByUsuario(String user);

    Mono<Void> deleteAllByUsuarioAndOrigen(String user, String origen);

    @Query("SELECT COUNT(*) FROM unrestricted_demand WHERE oficina = :office AND periodo BETWEEN :startDate AND :endDate")
    Mono<Long> countOffice(LocalDate startDate, LocalDate endDate, String office);

}