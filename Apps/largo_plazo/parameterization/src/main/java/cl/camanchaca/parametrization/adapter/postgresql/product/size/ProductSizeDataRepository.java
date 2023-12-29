package cl.camanchaca.parametrization.adapter.postgresql.product.size;

import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import cl.camanchaca.parametrization.adapter.postgresql.product.ProductData;
import io.r2dbc.spi.Parameter;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ProductSizeDataRepository extends ReactiveCrudRepository<ProductSizeData, UUID> {
    @Query("SELECT * FROM product_size WHERE products_id IN (:ids)")
    Flux<ProductSizeData> getAllByProductIdIn(@Param("ids") Flux<Integer> ids);


    Flux<ProductSizeData> findByProductIdAndAndSizeId(Integer productId, UUID sizeId);
    Flux<ProductSizeData> findAllByProductId(Integer codigo);
}
