package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import cl.camanchaca.business.repositories.optimization.DemandPeriodRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import cl.camanchaca.domain.models.optimization.DemandPeriod;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class GetAllDemandPeriodUseCase {

    private final DemandPeriodRepository demandPeriodRepository;
    private final UnrestrictedDemandRepository unrestrictedDemandRepository;
    private final PeriodRepository periodRepository;

    public Mono<ParametersResponse> apply(RequestParams requestParams, Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get(Constans.USER.getValue()))
                .collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }

                    Period period = periods.get(0);

                    return demandPeriodRepository
                            .getAllByPageAndSizeBetweenDates(requestParams.getPage(), requestParams.getSize(), period.getInitialPeriod(), period.getFinalPeriod(), requestParams.getDv())
                            .collectList()
                            .zipWith(demandPeriodRepository.countByDate(period.getInitialPeriod(), period.getFinalPeriod(), requestParams.getDv()))
                            .map(tuple2 -> ParametersResponse.of(tuple2.getT1(), tuple2.getT2()));
                });
    }

    public Flux<DemandPeriod> getAll(LocalDate initialDate, LocalDate endDate) {
        return demandPeriodRepository.getAllBetweenDate(initialDate, endDate);
    }

}
