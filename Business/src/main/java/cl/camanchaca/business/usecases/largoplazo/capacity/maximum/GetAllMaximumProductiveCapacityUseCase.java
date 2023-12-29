package cl.camanchaca.business.usecases.largoplazo.capacity.maximum;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.BaseScenarioRepository;
import cl.camanchaca.business.repositories.MaximumCapacityRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import cl.camanchaca.domain.models.capacity.maximum.MaximumCapacityValue;
import cl.camanchaca.domain.models.capacity.maximum.MaximumDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumCapacity;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetAllMaximumProductiveCapacityUseCase {

    private final MaximumCapacityRepository maximumRepository;

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
                            ).map(BasePeriodScenario::getPeriod)
                            .collectList()
                            .filter(o -> !o.isEmpty())
                            .flatMap(baseScenarioPeriod -> {
                                System.out.println("periods: " + baseScenarioPeriod);

                                return maximumRepository.getAllByPeriod(baseScenarioPeriod)
                                        .filter(dpc -> {
                                            System.out.println("maximum daily: " + dpc.toString());
                                            return dpc.getName() != null;
                                        })
                                        .groupBy(MaximumDailyProductiveCapacity::getName)
                                        .flatMap(groupedFlux -> groupedFlux.collectList().map(dailyProductiveCapacities -> {
                                            MaximumCapacity maximumCapacity = new MaximumCapacity();
                                            maximumCapacity.setName(groupedFlux.key());
                                            List<MaximumCapacityValue> capacities = dailyProductiveCapacities.stream()
                                                    .map(dpc -> new MaximumCapacityValue(dpc.getPeriod(), dpc.getMaximumCapacity()))
                                                    .filter(Objects::nonNull)
                                                    .collect(Collectors.toList());
                                            maximumCapacity.setCapacity(capacities);
                                            return maximumCapacity;
                                        })).collectList().map(maximumCapacities -> {
                                            return ParametersResponse.of(maximumCapacities, Long.valueOf(maximumCapacities.size()));
                                        });
                            });


                });
    }
}
