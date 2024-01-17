package cl.camanchaca.biomass.adapter.postgres.biomass.real;

import cl.camanchaca.biomass.mappers.postgres.RealBiomassMapper;
import cl.camanchaca.business.repositories.biomass.RealBiomassRepository;
import cl.camanchaca.domain.models.biomass.RealBiomass;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@AllArgsConstructor
public class RealBiomassDataAdapter implements RealBiomassRepository {

    private final RealBiomassDataRepository realBiomassDataRepository;

    @Override
    public Flux<RealBiomass> getAll() {
        return realBiomassDataRepository
                .findAll()
                .map(RealBiomassMapper::toRealBiomass);
    }

    @Override
    public Flux<RealBiomass> saveAll(List<RealBiomass> realBiomasses) {
        return Flux.fromIterable(realBiomasses)
                .concatMap(this::save);

    }

    @Override
    public Mono<RealBiomass> save(RealBiomass realBiomass) {

        return realBiomassDataRepository.existsById(realBiomass.getHu())
                .flatMap(exist -> Boolean.TRUE.equals(exist) ? realBiomassDataRepository.save(
                        RealBiomassMapper.toRealBiomassData(realBiomass)
                )
                        : realBiomassDataRepository.insertData(
                        RealBiomassMapper.toRealBiomassData(realBiomass)
                ))
                .then(Mono.defer(() -> Mono.just(realBiomass)));


    }

    @Override
    public Mono<Void> deleteAll() {
        return realBiomassDataRepository.deleteAll();
    }

    @Override
    public Flux<RealBiomass> getAllByPage(Integer size, Integer offset) {
        return realBiomassDataRepository
                .findAllByOffsetAndSize(offset, size)
                .map(RealBiomassMapper::toRealBiomass);
    }

    @Override
    public Mono<Long> count() {
        return realBiomassDataRepository.count();
    }
}
