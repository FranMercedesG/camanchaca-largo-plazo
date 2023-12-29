package cl.camanchaca.capacity.adapters.postgres.maximum.productivecapacity;

import cl.camanchaca.domain.models.capacity.maximum.MaximumTotalProductiveCapacity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface MaximumProductiveCapacityDataRepository extends ReactiveCrudRepository<MaximumProductiveCapacityData, UUID> {

    @Query("SELECT pc.productive_capacity_id as productiveCapacityId, pc.name as name, pdpc.period_daily_productive_capacity_id as periodDailyProductiveCapacityId, pdpc.maximum_capacity as maximumCapacity, pdpc.period as period FROM productive_capacity pc INNER JOIN period_daily_productive_capacity pdpc ON pc.productive_capacity_id = pdpc.productive_capacity_id")
    Flux<MaximumProductiveCapacityDTO> findAllWithDailyCapacity();;
    @Query("SELECT pc.productive_capacity_id as capacityid, " +
            "pc.name as name, " +
            "pdpc.period_daily_productive_capacity_id as perioddailyid, " +
            "pdpc.maximum_capacity as maximum, " +
            "pdpc.period as period " +
            "FROM productive_capacity pc " +
            "INNER JOIN period_daily_productive_capacity pdpc " +
            "ON pc.productive_capacity_id = pdpc.productive_capacity_id " +
            "WHERE pdpc.period IN (:periods)")
    Flux<MaximumProductiveCapacityDTO> findAllWithDailyCapacityByPeriods(List<LocalDate> periods);


    @Query("DELETE FROM productive_capacity pc WHERE pc.productive_capacity_id IN (:collect)")
    Mono<Void> deleteAllByIdIn(List<UUID> collect);


    @Query("SELECT pc.productive_capacity_id as productivecapacityid, " +
            "pc.name as name, " +
            "pdpc.period_productive_capacity_id as totalproductivecapacityid, " +
            "pdpc.maximum_capacity as maximumcapacity, " +
            "pdpc.period as period " +
            "FROM productive_capacity pc " +
            "INNER JOIN period_productive_capacity pdpc " +
            "ON pc.productive_capacity_id = pdpc.productive_capacity_id ")
    Flux<MaximumTotalProductiveCapacity> findAllTotalProductives();

    @Query("SELECT pc.productive_capacity_id as productivecapacityid, " +
            "pc.name as name, " +
            "pdpc.period_productive_capacity_id as totalproductivecapacityid, " +
            "pdpc.maximum_capacity as maximumcapacity, " +
            "pdpc.period as period " +
            "FROM productive_capacity pc " +
            "INNER JOIN period_productive_capacity pdpc " +
            "ON pc.productive_capacity_id = pdpc.productive_capacity_id "+
            "WHERE pdpc.period IN (:periods)")
    Flux<MaximumTotalProductiveCapacity> findAllTotalProductivesByPeriod(List<LocalDate> periods);
}
