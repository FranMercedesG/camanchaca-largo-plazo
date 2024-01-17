package cl.camanchaca.business.usecases.largoplazo.capacity.maximum;

import cl.camanchaca.business.generic.Constans;
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
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class GetAllTotalProductiveCapacityUseCase {

    private final MaximumCapacityRepository maximumRepository;

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
                                    currentPeriod.getInitialPeriod(),
                                    currentPeriod.getFinalPeriod()
                            )
                            .collectList()
                            .filter(o -> !o.isEmpty())
                            .flatMap(periods -> {

                                log.info("periods: " + periods);
                                return maximumRepository.getAllByPeriod(periods.stream().map(BasePeriodScenario::getPeriod).collect(Collectors.toList()))
                                        .filter(dpc -> {
                                            log.info("maximum daily: " + dpc.toString());
                                            return dpc.getName() != null;
                                        })
                                        .groupBy(MaximumDailyProductiveCapacity::getName)
                                        .flatMap(groupedFlux -> groupedFlux.collectList().map(dailyProductiveCapacities -> {
                                            Map<LocalDate, BasePeriodScenario> map = periods.stream()
                                                    .collect(Collectors.toMap(BasePeriodScenario::getPeriod, Function.identity()));
                                            MaximumCapacity maximumCapacity = new MaximumCapacity();
                                            maximumCapacity.setName(groupedFlux.key());
                                            List<MaximumCapacityValue> capacities = dailyProductiveCapacities.stream()
                                                    .map(dpc -> {
                                                        BigDecimal totalCapacity = BigDecimal.ZERO;
                                                        if (map.containsKey(dpc.getPeriod())) {
                                                            BasePeriodScenario period = map.get(dpc.getPeriod());
                                                            totalCapacity = calculateTotalCapacity(dpc.getMaximumCapacity(), period.getWorkDays(), period.getExtraDays());
                                                        }
                                                        return new MaximumCapacityValue(dpc.getPeriod(), totalCapacity);
                                                    })
                                                    .filter(Objects::nonNull)
                                                    .collect(Collectors.toList());
                                            maximumCapacity.setCapacity(capacities);
                                            return maximumCapacity;
                                        }))
                                        .collectList()
                                        .map(maximumCapacities ->
                                                ParametersResponse.of(maximumCapacities, Long.valueOf(maximumCapacities.size()))
                                            );
                            });
                });
    }




    public BigDecimal calculateTotalCapacity(BigDecimal productiveCapacity, Integer workingDays, Integer extraDays) {
        if (productiveCapacity == null || workingDays == null || extraDays == null) {
            throw new IllegalArgumentException("None of the parameters can be null");
        }

        int totalDays = workingDays + extraDays;
        return productiveCapacity.multiply(BigDecimal.valueOf(totalDays));
    }
}
