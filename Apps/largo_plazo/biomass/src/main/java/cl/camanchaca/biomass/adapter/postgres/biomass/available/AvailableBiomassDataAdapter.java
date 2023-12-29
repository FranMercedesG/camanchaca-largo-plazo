package cl.camanchaca.biomass.adapter.postgres.biomass.available;

import cl.camanchaca.biomass.mappers.postgres.AvailableBiomassMapper;
import cl.camanchaca.business.repositories.biomass.AvailableBiomassRepository;
import cl.camanchaca.domain.models.biomass.AvailableBiomass;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class AvailableBiomassDataAdapter implements AvailableBiomassRepository {

    private final AvailableBiomassDataRepository availableBiomassDataRepository;

    @Override
    public Flux<AvailableBiomass> getAll() {
        return availableBiomassDataRepository.findAll()
                .map(AvailableBiomassMapper::toAvailableBiomass);
    }

    @Override
    public Flux<AvailableBiomass> getAllByPage(Integer size, Integer offset) {
        return availableBiomassDataRepository.findAllByOffsetAndSize(offset, size)
                .map(AvailableBiomassMapper::toAvailableBiomass);
    }

    @Override
    public Flux<AvailableBiomass> getAllByDateAndPage(LocalDate date, Integer size, Integer offset) {
        return availableBiomassDataRepository
                .findAllByDateAndOffsetAndSize(date, offset, size)
                .map(AvailableBiomassMapper::toAvailableBiomass);
    }

    @Override
    public Flux<AvailableBiomass> getAllBetweenDateAndPage(LocalDate since, LocalDate until, Integer size, Integer offset) {
        return availableBiomassDataRepository
                .findAllBetweenDateAndOffsetAndSize(
                        since,
                        until,
                        offset,
                        size
                )
                .map(AvailableBiomassMapper::toAvailableBiomass);
    }

    @Override
    public Flux<AvailableBiomass> saveAll(List<AvailableBiomass> availableBiomasses) {
        return Flux.fromIterable(availableBiomasses).flatMap(this::saveAvailableBiomass);
    }

    @Override
    public Mono<AvailableBiomass> saveAvailableBiomass(AvailableBiomass availableBiomass) {
        return availableBiomassDataRepository.existsById(availableBiomass.getHu())
                .flatMap(exists ->
                        exists
                                ? availableBiomassDataRepository.save(AvailableBiomassMapper.toAvailableBiomassData(availableBiomass))
                                : availableBiomassDataRepository.insertData(AvailableBiomassMapper.toAvailableBiomassData(availableBiomass)))
                .then(Mono.defer(() -> Mono.just(availableBiomass)));
    }

    @Override
    public Mono<Long> count() {
        return availableBiomassDataRepository.count();
    }

    @Override
    public Mono<Long> countByDate(LocalDate date) {
        return availableBiomassDataRepository.countByDate(date);
    }

    @Override
    public Mono<Long> countBetweenDate(LocalDate since, LocalDate until) {
        return availableBiomassDataRepository.countBetweenDate(since, until);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return availableBiomassDataRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteAll() {
        return availableBiomassDataRepository.deleteAll();
    }

}
