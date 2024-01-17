package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.repositories.MonthlyPlanificationRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.optimization.MonthlyPlanification;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetVersionByPeriodSelectedUseCase {
    private final PeriodRepository periodRepository;

    private final MonthlyPlanificationRepository monthlyPlanificationRepository;

    public Mono<ParametersResponse> apply(Map<String, String> header) {

        return periodRepository.getSelectedPeriodByUser(header.get(Constans.USER.getValue()))
                .collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }
                    Period period = periods.get(0);
                   return monthlyPlanificationRepository.findByInitialAndFinalPeriod(period.getInitialPeriod(), period.getFinalPeriod())
                           .collectList()
                           .flatMap(month -> {
                               Set<Integer> version = month.stream().map(MonthlyPlanification::getVersion).collect(Collectors.toSet());
                               return Mono.just(ParametersResponse.of(Arrays.asList(version.toArray()), Long.valueOf(version.size())));
                   });
                });


    }
}
