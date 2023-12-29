package cl.camanchaca.biomass.adapter.postgres.product.group;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;
@Repository
public interface ProductGroupDataRepository extends ReactiveCrudRepository<ProductGroupData, UUID> {

    Flux<ProductGroupData> findAllByProductId(Integer codigo);
    Flux<ProductGroupData> findByProductIdAndGroupId(Integer codigo, UUID groupId);
}