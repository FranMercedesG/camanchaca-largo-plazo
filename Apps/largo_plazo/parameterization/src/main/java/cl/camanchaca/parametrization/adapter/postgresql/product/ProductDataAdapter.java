package cl.camanchaca.parametrization.adapter.postgresql.product;

import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.domain.models.Product;
import cl.camanchaca.parametrization.mappers.ProductMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductDataAdapter implements ProductRepository {

    private final ProductDataRepository productDataRepository;

    @Override
    public Mono<Product> findById(Integer id) {
        return productDataRepository.findById(id)
                .map(ProductMapper::toProduct);
    }

    @Override
    public Flux<Product> findAllByCodigo(List<Integer> ids) {
        return productDataRepository.findAllByCodigoList(ids)
                .map(ProductMapper::toProduct);
    }

    @Override
    public Mono<Product> saveProduct(Product product) {
        return productDataRepository.existsById(product.getCodigo())
                .flatMap(exists ->
                        Boolean.TRUE.equals(exists)
                                ? productDataRepository.save(ProductMapper.toProductData(product))
                                : productDataRepository.insertProductData(ProductMapper.toProductData(product)))
                .then(Mono.defer(() -> Mono.just(product)));
    }

    @Override
    public Flux<Product> saveProducts(Flux<Product> products) {
        return products
                .parallel()
                .runOn(Schedulers.parallel())
                .flatMap(this::saveProduct)
                .sequential();
    }

    @Override
    public Flux<Product> findAll() {
        return productDataRepository.findAll()
                .map(ProductMapper::toProduct);
    }

    @Override
    public Flux<Product> getByPageAndSize(Integer page, Integer size) {
        int offset = (page - 1) * size;
        return productDataRepository.findAllByOffsetAndSize(offset, size)
                .map(ProductMapper::toProduct);
    }

    @Override
    public Mono<Long> count() {
        return productDataRepository.count();
    }
}
