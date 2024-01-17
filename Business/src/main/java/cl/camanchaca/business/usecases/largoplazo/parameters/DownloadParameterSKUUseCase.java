package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.repositories.DownloadSKUParameterRepository;
import cl.camanchaca.business.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;
@AllArgsConstructor
@Slf4j
public class DownloadParameterSKUUseCase implements Supplier<Mono<byte[]>> {

    private final DownloadSKUParameterRepository downloadExelRepository;
    private final ProductRepository productRepository;
    @Override
    public Mono<byte[]> get() {
        return productRepository
                .findAll()
                .collectList()
                .flatMap(downloadExelRepository::getBytes)
                .onErrorContinue((throwable, o) -> log.info("error DownloadSku {}", o));
    }
}
