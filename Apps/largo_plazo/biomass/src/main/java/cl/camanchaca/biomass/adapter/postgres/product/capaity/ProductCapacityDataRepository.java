package cl.camanchaca.biomass.adapter.postgres.product.capaity;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ProductCapacityDataRepository extends ReactiveCrudRepository<ProductCapacityData, UUID> {

    @Query("SELECT * FROM product_capacity OFFSET :offset LIMIT :size")
    Flux<ProductCapacityData> findAllByOffsetAndSize(@Param("offset") Integer offset, @Param("size") Integer size);

    Flux<ProductCapacityData> findByProductIdAndAndProductiveCapacityId(Integer productId, UUID productiveCapacityId);
    Flux<ProductCapacityData> findAllByProductId(Integer codigo);
}
