package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.Product;
import reactor.core.publisher.Mono;

import java.util.List;

public interface DownloadSKUParameterRepository {

    Mono<byte[]> getBytes(List<Product> data);

}
