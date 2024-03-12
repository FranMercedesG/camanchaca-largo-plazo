package cl.camanchaca.business.repositories.optimization;

import cl.camanchaca.domain.models.optimization.DemandPeriod;
import cl.camanchaca.domain.models.optimization.Period;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface DemandPeriodRepository {
    Flux<DemandPeriod> getAll();

    Flux<DemandPeriod> getAllBetweenDate(LocalDate initialDate, LocalDate endDate);

    Flux<DemandPeriod> getAllByPageAndSizeBetweenDates(Integer page, Integer size, LocalDate initialDate, LocalDate finalDate, String dv);

    Mono<Long> countByDate(LocalDate startDate, LocalDate endDate, String dv);

    Mono<Long> count();

    Flux<DemandPeriod> save(DemandPeriod demandPeriod);

    Mono<Period> getPeriodByPeriodAndDemandUUID(LocalDate period, UUID demandUUID);

    Flux<Period> getPeriodByPeriodINAndDemandUUID(List<LocalDate> period, UUID demandUUID);
}
