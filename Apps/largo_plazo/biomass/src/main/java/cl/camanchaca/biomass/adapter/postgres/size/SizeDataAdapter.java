package cl.camanchaca.biomass.adapter.postgres.size;

import cl.camanchaca.biomass.mappers.postgres.SizeMapper;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.domain.models.Size;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return sizeDataRepository.findAllBySpecie(specie)
                .map(SizeMapper::toSize);
    }

    @Override
    public Mono<Size> getById(UUID id) {
        return sizeDataRepository.findById(id)
                .map(SizeMapper::toSize);
    }

    @Override
    public Flux<Size> sizeAll(List<Size> size) {

        Flux<SizeData> sizeDataFlux = Flux.fromIterable(
                size
                        .stream().map(SizeMapper::toSizeData)
                        .collect(Collectors.toList())
        );
        return sizeDataRepository.saveAll(sizeDataFlux)
                .map(SizeMapper::toSize);
    }

    @Override
    public Mono<Long> count() {
        return sizeDataRepository.count();
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return sizeDataRepository.deleteById(id);
    }

    @Override
    public Mono<Size> getByRangesAndUnitAndQualityAndSpecie(Size size) {
        return sizeDataRepository.findAllByMinRangeAndMaxRangeAndUnitAndSpecieAndPieceType(
                        size.getInitialRange(),
                        size.getFinalRange(),
                        size.getUnit().name(),
                        size.getSpecies(),
                        size.getPieceType()
                )
                .next()
                .map(SizeMapper::toSize);
    }
}
