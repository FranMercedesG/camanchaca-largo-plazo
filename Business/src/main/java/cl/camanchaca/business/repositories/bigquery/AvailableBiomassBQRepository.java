package cl.camanchaca.business.repositories.bigquery;

import cl.camanchaca.domain.models.biomass.AvailableBiomass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface AvailableBiomassBQRepository {

    Flux<AvailableBiomass> getAll() throws InterruptedException;
    Flux<AvailableBiomass> getByDateAndPage(LocalDate date, Integer size, Integer offset) throws InterruptedException;
    Flux<AvailableBiomass> getByDate(LocalDate date) throws InterruptedException;
    Flux<AvailableBiomass> getBetweenDateAndPage(LocalDate since, LocalDate until,Integer size, Integer offset) throws InterruptedException;

    Mono<Long> count() throws InterruptedException;
    Mono<Long> countByDate(LocalDate date) throws InterruptedException;
    Mono<Long> countBetweemDate(LocalDate since, LocalDate until) throws InterruptedException;

}
