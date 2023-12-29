package cl.camanchaca.biomass.adapter.postgres.biomass.real;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RealBiomassDataRepository extends ReactiveCrudRepository<RealBiomassData, String> {

    @Query("INSERT INTO real_biomass " +
            "(hu, plant, quality, size, pieces, " +
            "weight, specie) " +
            "VALUES (:#{#data.hu}, :#{#data.plant}, :#{#data.quality}, " +
            ":#{#data.size}, :#{#data.pieces}, :#{#data.weight}, :#{#data.specie}) ")
    Mono<Void> insertData(RealBiomassData data);

    @Query("SELECT * FROM real_biomass OFFSET :offset LIMIT :size")
    Flux<RealBiomassData> findAllByOffsetAndSize(@Param("offset") Integer offset, @Param("size") Integer size);

}
