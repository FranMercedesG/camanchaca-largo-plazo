package cl.camanchaca.business.usecases.largoplazo.capacity.maximum;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.BaseScenarioRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.TotalMaximumRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import cl.camanchaca.domain.models.capacity.maximum.MaximumCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumCapacityValue;
import cl.camanchaca.domain.models.capacity.maximum.MaximumTotalProductiveCapacity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class GetAllTotalSavedUseCase {

    private final TotalMaximumRepository totalMaximumRepository;

    private final BaseScenarioRepository baseScenarioRepository;

    private final PeriodRepository periodRepository;

    public Mono<ParametersResponse> apply(RequestParams requestParams, Map<String, String> header) {

        return periodRepository.getSelectedPeriodByUser(header.get(Constans.USER.getValue()))
                .collectList()
                .flatMap(selectedPeriods -> {
                    if (selectedPeriods.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }

                    Period currentPeriod = selectedPeriods.get(0);

                    return baseScenarioRepository.getAllBetweenDates(
                            currentPeriod.getInitialPeriod(), currentPeriod.getFinalPeriod()
                            )
                            .collectList()
                            .filter(o -> !o.isEmpty())
                            .flatMap(periods -> totalMaximumRepository.getAllTotalProductiveByPeriod(periods.stream().map(BasePeriodScenario::getPeriod).collect(Collectors.toList()))
                                    .doOnNext(data -> log.info(data.toString()))
                                    .groupBy(MaximumTotalProductiveCapacity::getName)
                                    .flatMap(groupedFlux -> groupedFlux.collectList().map(totalProductive -> {
                                        MaximumCapacity maximumCapacity = new MaximumCapacity();
                                        maximumCapacity.setName(groupedFlux.key());
                                        List<MaximumCapacityValue> capacities = totalProductive.stream()
                                                .map(dpc -> new MaximumCapacityValue(dpc.getPeriod(), dpc.getMaximumcapacity()))
                                                .collect(Collectors.toList());
                                        maximumCapacity.setCapacity(capacities);
                                        return maximumCapacity;
                                    })).collectList().map(maximumCapacities -> ParametersResponse.of(maximumCapacities, (long) maximumCapacities.size())));
                });
    }


}

