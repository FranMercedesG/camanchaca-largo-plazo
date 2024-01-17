package cl.camanchaca.business.usecases.largoplazo.capacity.minimum;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.BaseScenarioRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.TotalMinimumRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacityValue;
import cl.camanchaca.domain.models.capacity.minimum.MinimumTotalProductiveCapacity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class GetAllTotalMinimumSavedUseCase {
    private final TotalMinimumRepository totalMinimumRepository;
    private final BaseScenarioRepository baseScenarioRepository;
    private final PeriodRepository periodRepository;

    public Mono<ParametersResponse>apply(RequestParams requestParams, Map<String, String> header) {

        return periodRepository.getSelectedPeriodByUser(header.get(Constans.USER.getValue()))
                .collectList()
                .flatMap(periodsSelected -> {
                    if (periodsSelected.isEmpty()) {
                        return Mono.empty();
                    }

                    Period periodSel = periodsSelected.get(0);

                    return baseScenarioRepository.getAllBetweenDates(
                             periodSel.getInitialPeriod(), periodSel.getFinalPeriod()
                            )
                            .collectList()
                            .filter(o -> !o.isEmpty())
                            .flatMap(periods -> totalMinimumRepository.getAllTotalProductiveByPeriod(periods.stream().map(BasePeriodScenario::getPeriod).collect(Collectors.toList()))
                                    .doOnNext(minimumTotalProductiveCapacity -> log.info(minimumTotalProductiveCapacity.toString()))
                                    .groupBy(MinimumTotalProductiveCapacity::getName)
                                    .flatMap(groupedFlux -> groupedFlux.collectList().map(totalProductive -> {
                                        MinimumCapacity maximumCapacity = new MinimumCapacity();
                                        maximumCapacity.setName(groupedFlux.key());
                                        List<MinimumCapacityValue> capacities = totalProductive.stream()
                                                .map(dpc -> new MinimumCapacityValue(dpc.getPeriod(), dpc.getMaximumcapacity()))
                                                .collect(Collectors.toList());
                                        maximumCapacity.setCapacity(capacities);
                                        return maximumCapacity;
                                    })).collectList().map(maximumCapacities -> ParametersResponse.of(maximumCapacities, (long) maximumCapacities.size())));
                });
    }


}
