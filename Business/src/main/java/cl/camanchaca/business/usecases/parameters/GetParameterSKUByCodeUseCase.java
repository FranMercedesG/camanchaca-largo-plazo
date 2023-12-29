package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.domain.models.Product;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GetParameterSKUByCodeUseCase extends Usecase<Integer, Mono<Product>> {

    private final ProductRepository productRepository;

    @Override
    public Mono<Product> apply(Integer code) {
        return productRepository.findById(code);
    }
}
