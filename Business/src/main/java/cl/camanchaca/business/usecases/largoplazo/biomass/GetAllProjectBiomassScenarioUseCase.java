package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.Constans;
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
public class GetAllProjectBiomassScenarioUseCase {

    private final PeriodRepository periodRepository;

    private final ProjectedBiomasBQRepository biomasBQRepository;
    public Mono<ParametersResponse> apply(RequestParams requestParams, Map<String, String> headerInfo) {
        return periodRepository.getSelectedPeriodByUser(headerInfo.get(Constans.USER.getValue()))
                .collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }

                    Period period = periods.get(0);

                    try {

                        if(requestParams.getSpecie().equalsIgnoreCase("ATLANTIC")){
                            requestParams.setSpecie("SALAR");
                        }

                        return biomasBQRepository.getAllByScenario(period.getInitialPeriod(), period.getFinalPeriod(), requestParams.getSelectedScenario(), requestParams.getSpecie() , requestParams.getSize(), requestParams.getPage()
                                )
                                .collectList()
                                .map(unrestrictedDemandList -> ParametersResponse.of(unrestrictedDemandList, (long) unrestrictedDemandList.size()));
                    } catch (InterruptedException e) {
                        log.error("Error in GetAllProjectBiomassScenarioUseCase", e);
                        Thread.currentThread().interrupt();
                        return Mono.error(new RuntimeException(e));
                    }
                });
    }


}
