package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface BaseScenarioRepository{
    Mono<Void> deleteAll();

    Flux<BasePeriodScenario> getAll();

    Flux<BasePeriodScenario> getAllBetweenDatesAndPageAndSize(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate);

    Flux<BasePeriodScenario> getAllBetweenDates(LocalDate initialDate, LocalDate finalDate);

    Flux<BasePeriodScenario> saveAll(Flux<BasePeriodScenario> body);

}
