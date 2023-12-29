package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.optimization.Optimization;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DownloadActiveOptimizationRepository {
    Mono<byte[]> downloadActiveOptimization(Flux<Optimization> optimization);

}
