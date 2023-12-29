package cl.camanchaca.parametrization.validations;

import cl.camanchaca.domain.models.Product;
import cl.camanchaca.generics.errors.InfrastructureError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class ValidationProduct {

    public static Flux<Product> validateProduct(Flux<Product> products) {
        return products.map(product -> {
                    Stream.of(
                            product.getCodigo()
                    ).forEach(Objects::requireNonNull);
                    return product;
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }


    private ValidationProduct() {
    }
}
