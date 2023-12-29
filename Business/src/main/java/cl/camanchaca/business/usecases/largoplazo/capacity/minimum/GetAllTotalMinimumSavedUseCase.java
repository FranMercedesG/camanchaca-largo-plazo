package cl.camanchaca.business.usecases.largoplazo.capacity.minimum;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.BaseScenarioRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.TotalMinimumRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import cl.camanchaca.domain.models.capacity.maximum.MaximumCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumCapacityValue;
import cl.camanchaca.domain.models.capacity.minimum.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetAllTotalMinimumSavedUseCase {
    private final TotalMinimumRepository totalMinimumRepository;
    private final BaseScenarioRepository baseScenarioRepository;
    private final PeriodRepository periodRepository;

    public Mono<ParametersResponse>apply(RequestParams requestParams, Map<String, String> header) {

        return periodRepository.getSelectedPeriodByUser(header.get("user"))
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
                                    .doOnNext(System.out::println)
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
