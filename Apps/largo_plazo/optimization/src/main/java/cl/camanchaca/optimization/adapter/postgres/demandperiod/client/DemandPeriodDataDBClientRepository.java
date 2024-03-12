package cl.camanchaca.optimization.adapter.postgres.demandperiod.client;

import cl.camanchaca.domain.models.optimization.DemandPeriod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

public interface DemandPeriodDataDBClientRepository {
    Flux<DemandPeriod> getAllByPageAndSize(LocalDate startDate, LocalDate endDate, Integer page, Integer size, String dv);

    Mono<Long> countDemandPeriodData(LocalDate startDate, LocalDate endDate, String dv);

    Flux<DemandPeriod> getAllBetweenDate(LocalDate startDate, LocalDate endDate);

    Flux<DemandPeriod> getAll();

}
