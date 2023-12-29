package cl.camanchaca.business.usecases.parameters;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.domain.models.Product;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@AllArgsConstructor
public class SaveSKUUseCase extends Usecase<List<Product>, Flux<Product>> {


    private final ProductRepository productRepository;

    @Override
    public Flux<Product> apply(List<Product> productFlux) {
        return productRepository.saveProducts(
                        Flux.fromIterable(productFlux)
                )
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError("Error con los datos de guardado"));
                });
    }
}
