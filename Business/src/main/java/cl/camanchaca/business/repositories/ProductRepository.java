package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductRepository {

    Mono<Product> findById(Integer id);

    Flux<Product> findAllByCodigo(List<Integer> ids);

    Mono<Product> saveProduct(Product product);
    Flux<Product> saveProducts(Flux<Product> products);
    Flux<Product> findAll();
    Flux<Product> getByPageAndSize(Integer page, Integer size);
    Mono<Long> count();


}
