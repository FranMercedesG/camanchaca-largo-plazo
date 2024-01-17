package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class GetAllUnrestrictedDemandAdminUseCase {

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

                    var offset = (requestParams.getPage() - 1) * requestParams.getSize();

                    return unrestrictedDemandRepository.getAllBetweenDatesAndPageAndSizeAndFilters(
                                    offset, requestParams.getSize(), period.getInitialPeriod(), period.getFinalPeriod(), requestParams.getSpecie(), requestParams.getFamily()
                            ).collectList()
                            .flatMap(unrestrictedDemands -> Mono.zip(Mono.just(unrestrictedDemands),
                                    unrestrictedDemandRepository.countFilters(period.getInitialPeriod(),
                                            period.getFinalPeriod(),
                                            requestParams.getSpecie(),
                                            requestParams.getFamily())))
                            .map(tuple -> {
                                List<UnrestrictedDemand> unrestrictedDemandList = tuple.getT1();

                                return ParametersResponse.of(unrestrictedDemandList, tuple.getT2());
                            });
                });
    }
}
