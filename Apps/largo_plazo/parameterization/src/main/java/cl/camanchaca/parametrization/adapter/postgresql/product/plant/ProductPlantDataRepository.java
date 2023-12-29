package cl.camanchaca.parametrization.adapter.postgresql.product.plant;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProductPlantDataRepository extends ReactiveCrudRepository<ProductPlantData, UUID> {

    Flux<ProductPlantData> findByPlantId(UUID plantId);
    Flux<ProductPlantData> findByProductId(Integer productId);
    Flux<ProductPlantData> findByProductIdAndPlantId(Integer productId, UUID plantId);


    Mono<Long> countByPlantId(UUID plantId);


    @Query("SELECT COUNT(plant_id) FROM product_plant WHERE plant_id = :plantId AND status = TRUE")
    Mono<Long> countByPlantIdAndStatusIsTrue(@Param("plantId") Long plantId);

}
