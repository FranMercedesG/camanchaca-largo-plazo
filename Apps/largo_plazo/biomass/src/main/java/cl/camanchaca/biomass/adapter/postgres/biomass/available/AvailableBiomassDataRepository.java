package cl.camanchaca.biomass.adapter.postgres.biomass.available;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface AvailableBiomassDataRepository extends ReactiveCrudRepository<AvailableBiomassData, String> {

    @Query("SELECT * FROM avalaible_biomass OFFSET :offset LIMIT :size")
    Flux<AvailableBiomassData> findAllByOffsetAndSize(@Param("offset") Integer offset, @Param("size") Integer size);


    @Query("SELECT * FROM avalaible_biomass WHERE CAST(fecpro as DATE) = :fecha OFFSET :offset LIMIT :size")
    Flux<AvailableBiomassData> findAllByDateAndOffsetAndSize(@Param("fecha") LocalDate fecha,
                                                             @Param("offset") Integer offset,
                                                             @Param("size") Integer size);

    @Query("SELECT * FROM avalaible_biomass WHERE CAST(fecpro as DATE) BETWEEN :since AND :until OFFSET :offset LIMIT :size")
    Flux<AvailableBiomassData> findAllBetweenDateAndOffsetAndSize(@Param("since") LocalDate since,
                                                                  @Param("until") LocalDate until,
                                                                  @Param("offset") Integer offset,
                                                                  @Param("size") Integer size);


    @Query("INSERT INTO avalaible_biomass " +
            "(hu, external_hu, type, size, quality, " +
            "header, manufacturing_order, batch, folio, center, " +
            "fecpro, permanency, pieces, weight, specie) " +
            "VALUES (:#{#data.hu}, :#{#data.externalHu}, :#{#data.type}, " +
            ":#{#data.size}, :#{#data.quality}, :#{#data.header}, :#{#data.manufacturingOrder}, " +
            ":#{#data.batch}, :#{#data.folio}, :#{#data.center}, :#{#data.fecpro}, " +
            ":#{#data.permanency}, :#{#data.pieces}, :#{#data.weight}, :#{#data.specie})")
    Mono<Void> insertData(AvailableBiomassData data);


    @Query("SELECT COUNT(*) FROM avalaible_biomass WHERE CAST(fecpro as DATE) = :fecha ")
    Mono<Long> countByDate(@Param("fecha") LocalDate date);

    @Query("SELECT COUNT(*) FROM avalaible_biomass WHERE CAST(fecpro as DATE) BETWEEN :since AND :until")
    Mono<Long> countBetweenDate(@Param("since") LocalDate since,
                                @Param("until") LocalDate until);

}
