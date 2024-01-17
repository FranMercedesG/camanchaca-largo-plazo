package cl.camanchaca.biomass.adapter.postgres.groupsizemax;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface GroupMaxSizeDataRepository extends ReactiveCrudRepository<GroupMaxSizeData, UUID> {

    @Query("SELECT * FROM max_size_group WHERE period >= :initialPeriod AND period <= :finalPeriod AND group_id IN (:groupsUUID)")
    Flux<GroupMaxSizeData> findGroupMaxSizeDataByPeriodAndGroups(@Param("initialPeriod") LocalDate initialPeriod,
                                                                 @Param("finalPeriod") LocalDate finalPeriod, @Param("groupsUUID") List<UUID> groupsUUID);
    @Query("INSERT INTO max_size_group " +
            "(group_id, size_id, period, maximum_limit) " +
            "VALUES (:#{#data.groupId}, :#{#data.sizeId}, " +
            ":#{#data.period}, :#{#data.maximumLimit})")
    Mono<Void> insertData(@Param("data") GroupMaxSizeData data);
}
