package cl.camanchaca.parametrization.adapter.postgresql.product.minimum;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ProductMinimumDataRepository extends ReactiveCrudRepository<ProductMinimumData, UUID> {
    Flux<ProductMinimumData> findAllByProductId(Integer codigo);
    Flux<ProductMinimumData> findByProductIdAndMinimumId(Integer codigo, UUID minimumId);


}
