package cl.camanchaca.business.usecases.largoplazo.capacity.productivedays;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.BaseScenarioRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.domain.models.Period;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class GetAllProductiveDaysUseCase {

    private final BaseScenarioRepository baseScenarioRepository;
    private final PeriodRepository periodRepository;

    public Mono<ParametersResponse> apply(RequestParams requestParams, Map<String, String> header) {

        return periodRepository.getSelectedPeriodByUser(header.get("user"))
                .collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }

                    Period period = periods.get(0);

                    return baseScenarioRepository.getAllBetweenDates(
                             period.getInitialPeriod(), period.getFinalPeriod()
                    ).collectList().flatMap(baseScenario -> {
                        return Mono.just(ParametersResponse.of(baseScenario, Long.valueOf(baseScenario.size())));
                    });
                });


    }
}
