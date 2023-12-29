package cl.camanchaca.business.repositories.bigquery;

import cl.camanchaca.domain.models.biomass.RealBiomass;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface RealBiomassBQRepository {

    Flux<RealBiomass> getAll() throws InterruptedException;


    Flux<RealBiomass> getByDateAndPage(LocalDate date, Integer size, Integer offset) throws InterruptedException;
    Flux<RealBiomass> getByDate(LocalDate date);

    Mono<Long> count() throws InterruptedException;
    Mono<Long> countByDate(LocalDate date) throws InterruptedException;


}
