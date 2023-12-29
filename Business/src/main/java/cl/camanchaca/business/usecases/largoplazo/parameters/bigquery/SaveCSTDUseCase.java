package cl.camanchaca.business.usecases.largoplazo.parameters.bigquery;

import cl.camanchaca.business.repositories.BigqueryCSTDRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import cl.camanchaca.domain.models.Product;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

@AllArgsConstructor
public class SaveCSTDUseCase {

    private final BigqueryCSTDRepository bigqueryCSTDRepository;
    private final ProductRepository productRepository;

    public Flux<Product> SaveSKU() {
        try {
            return bigqueryCSTDRepository
                    .getAll()
                    .flatMap(product -> productRepository.saveProduct(product));

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
