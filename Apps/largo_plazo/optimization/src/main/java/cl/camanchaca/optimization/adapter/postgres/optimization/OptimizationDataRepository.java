package cl.camanchaca.optimization.adapter.postgres.optimization;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

@Repository
public interface OptimizationDataRepository extends ReactiveCrudRepository<OptimizationData, Object> {

    @Query("select omp.unrestricted_demand_id, " +
            "       omp.size_id, " +
            "       omp.period, " +
            "       omp.kilo_wfe, " +
            "       ud.products_id," +
            "       omp.order_monthly_planification_id " +
            "from order_monthly_planification omp " +
            "inner join unrestricted_demand ud " +
            "    on (omp.unrestricted_demand_id=ud.unrestricted_demand_id) " +
            "inner join monthly_planification mp " +
            "    on (omp.monthly_planification_id=mp.monthly_planification_id) " +
            "where mp.active is true " +
            "AND omp.period BETWEEN :from AND :until")
    Flux<OptimizationData> getActiveByMonth(@Param("from") LocalDate from, @Param("until") LocalDate until);

    @Query("select omp.unrestricted_demand_id, " +
            "       omp.size_id, " +
            "       omp.period, " +
            "       omp.kilo_wfe, " +
            "       ud.codigo," +
            "       omp.order_monthly_planification_id " +
            "from order_monthly_planification omp " +
            "inner join unrestricted_demand ud " +
            "    on (omp.unrestricted_demand_id=ud.unrestricted_demand_id) " +
            "inner join monthly_planification mp " +
            "    on (omp.monthly_planification_id=mp.monthly_planification_id) " +
            "where mp.version = :version " +
            "AND omp.period BETWEEN :from AND :until")
    Flux<OptimizationData> getByMonthAndVersion(
            @Param("version") int version,
            @Param("from") LocalDate from,
            @Param("until") LocalDate until
    );

}
