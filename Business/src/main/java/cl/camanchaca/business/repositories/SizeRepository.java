package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.Size;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface SizeRepository {
    Flux<Size> getAll();
    Flux<Size> getBySpecie(String specie);
    Mono<Size> getById(UUID id);
    Flux<Size> sizeAll(List<Size> size);
    Mono<Long> count();
    Mono<Void> deleteById(UUID id);

    Mono<Size> getByRangesAndUnitAndQualityAndSpecie(Size size);
}
