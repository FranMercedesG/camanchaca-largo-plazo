package cl.camanchaca.capacity.adapters.postgres.minimum.minimumcapacity;

import cl.camanchaca.domain.models.capacity.maximum.MaximumTotalProductiveCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumTotalProductiveCapacity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
@Repository
public interface MinimumDataRepository extends ReactiveCrudRepository<MinimumData, UUID> {
    @Query("SELECT pc.minimum_id as capacityid, " +
            "pc.name as name, " +
            "pdpc.minimum_daily_period_id as perioddailyid, " +
            "pdpc.minimum_value as maximum, " +
            "pdpc.period as period " +
            "FROM minimum pc " +
            "INNER JOIN minimum_daily_period pdpc " +
            "ON pc.minimum_id = pdpc.minimum_id " +
            "WHERE pdpc.period IN (:periods)")
    Flux<MinimumCapacityDTO> findAllWithDailyCapacityByPeriods(List<LocalDate> periods);

    @Query("DELETE FROM minimum pc WHERE pc.minimum_id IN (:collect)")
    Mono<Void> deleteAllByIdIn(List<UUID> collect);

    @Query("SELECT pc.minimum_id as productivecapacityid, " +
            "pc.name as name, " +
            "pdpc.minimum_period_id as totalproductivecapacityid, " +
            "pdpc.minimum_value as maximumcapacity, " +
            "pdpc.period as period " +
            "FROM minimum pc " +
            "INNER JOIN minimum_period pdpc " +
            "ON pc.minimum_id = pdpc.minimum_id ")
    Flux<MinimumTotalProductiveCapacity> findAllTotalProductives();

    @Query("SELECT pc.minimum_id as productivecapacityid, " +
            "pc.name as name, " +
            "pdpc.minimum_period_id as totalproductivecapacityid, " +
            "pdpc.minimum_value as maximumcapacity, " +
            "pdpc.period as period " +
            "FROM minimum pc " +
            "INNER JOIN minimum_period pdpc " +
            "ON pc.minimum_id = pdpc.minimum_id "+
            "WHERE pdpc.period IN (:periods)")
    Flux<MinimumTotalProductiveCapacity> findAllTotalProductivesByPeriod(List<LocalDate> periods);

}
