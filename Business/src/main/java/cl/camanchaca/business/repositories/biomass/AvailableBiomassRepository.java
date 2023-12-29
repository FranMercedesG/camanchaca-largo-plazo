package cl.camanchaca.business.repositories.biomass;

import cl.camanchaca.domain.models.biomass.AvailableBiomass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface AvailableBiomassRepository {

    Flux<AvailableBiomass> getAll();
    Flux<AvailableBiomass> getAllByPage(Integer size, Integer offset);
    Flux<AvailableBiomass> getAllByDateAndPage(LocalDate date, Integer size, Integer offset);
    Flux<AvailableBiomass> getAllBetweenDateAndPage(LocalDate since, LocalDate until, Integer size, Integer offset);
    Flux<AvailableBiomass> saveAll(List<AvailableBiomass> availableBiomasses);
    Mono<AvailableBiomass> saveAvailableBiomass(AvailableBiomass availableBiomass);

    Mono<Long> count();
    Mono<Long> countByDate(LocalDate date);
    Mono<Long> countBetweenDate(LocalDate since, LocalDate until);

    Mono<Void> deleteById(String id);
    Mono<Void> deleteAll();

}
