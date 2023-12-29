package cl.camanchaca.business.usecases.largoplazo.capacity.minimum;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.BaseScenarioRepository;
import cl.camanchaca.business.repositories.MinimumCapacityRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacityValue;
import cl.camanchaca.domain.models.capacity.minimum.MinimumDailyProductiveCapacity;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetAllMinimumProductiveCapacityUseCase {

    private final MinimumCapacityRepository minimumCapacityRepository;

    private final BaseScenarioRepository baseScenarioRepository;

    private final PeriodRepository periodRepository;

    public Mono<ParametersResponse> apply(RequestParams requestParams, Map<String, String> header) {

        return periodRepository.getSelectedPeriodByUser(header.get("user"))
                .collectList()
                .flatMap(periodsSelected -> {
                    if (periodsSelected.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }

                    Period periodSel = periodsSelected.get(0);

                    return baseScenarioRepository.getAllBetweenDates(
                                    periodSel.getInitialPeriod(), periodSel.getFinalPeriod()
                            ).map(BasePeriodScenario::getPeriod)
                            .collectList()
                            .filter(o -> !o.isEmpty())
                            .flatMap(periods -> {
                                System.out.println("periods: " + periods);
                                return minimumCapacityRepository.getAllByPeriod(periods)
                                        .filter(dpc -> {
                                            System.out.println("minimum daily: " + dpc.toString());
                                            return dpc.getName() != null;
                                        })
                                        .groupBy(MinimumDailyProductiveCapacity::getName)
                                        .flatMap(groupedFlux -> groupedFlux.collectList().map(dailyProductiveCapacities -> {
                                            MinimumCapacity minimumCapacity = new MinimumCapacity();
                                            minimumCapacity.setName(groupedFlux.key());
                                            List<MinimumCapacityValue> capacities = dailyProductiveCapacities.stream()
                                                    .map(dpc -> new MinimumCapacityValue(dpc.getPeriod(), dpc.getMaximumCapacity()))
                                                    .filter(Objects::nonNull)
                                                    .collect(Collectors.toList());
                                            minimumCapacity.setCapacity(capacities);
                                            return minimumCapacity;
                                        })).collectList().map(minimumCapacities -> {
                                            return ParametersResponse.of(minimumCapacities, Long.valueOf(minimumCapacities.size()));
                                        });
                            });
                });
    }


}
