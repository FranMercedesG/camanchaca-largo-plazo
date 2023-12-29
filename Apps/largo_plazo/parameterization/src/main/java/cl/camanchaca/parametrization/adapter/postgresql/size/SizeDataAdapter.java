package cl.camanchaca.parametrization.adapter.postgresql.size;

import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.parametrization.mappers.SizeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SizeDataAdapter implements SizeRepository {

    private final SizeDataRepository sizeDataRepository;

    @Override
    public Flux<Size> getAll() {
        return sizeDataRepository.findAll()
                .map(SizeMapper::toSize);
    }

    @Override
    public Flux<Size> getBySpecie(String specie) {
        return null;
    }

    @Override
    public Mono<Size> getById(UUID id) {
        return null;
    }

    @Override
    public Flux<Size> sizeAll(List<Size> size) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return null;
    }

    @Override
    public Mono<Size> getByRangesAndUnitAndQualityAndSpecie(Size size) {
        return null;
    }
}
