package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.bigquery.ProjectedBiomasBQRepository;
import cl.camanchaca.domain.models.Period;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class GetAllBiomassScenariosUseCase {

    private final PeriodRepository periodRepository;

    private final ProjectedBiomasBQRepository biomasBQRepository;

    public Mono<ParametersResponse> apply(RequestParams requestParams, Map<String, String> headerInfo) {
        return periodRepository.getSelectedPeriodByUser(headerInfo.get("user"))
                .collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }

                    Period period = periods.get(0);

                    try {
                        return biomasBQRepository.getAllScenarios(period.getInitialPeriod(), period.getFinalPeriod(), requestParams.getSize(), requestParams.getPage()
                                )
                                .collectList()
                                .map(unrestrictedDemandList -> ParametersResponse.of(unrestrictedDemandList, (long) unrestrictedDemandList.size()));
                    } catch (InterruptedException e) {
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }
}
