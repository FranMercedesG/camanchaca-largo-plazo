package cl.camanchaca.business.usecases.largoplazo.capacity.minimum;

import cl.camanchaca.business.generic.Constans;
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
public class GetAllTotalMinimumProductiveCapacityUseCase {

    private final MinimumCapacityRepository minimumRepository;

    private final BaseScenarioRepository baseScenarioRepository;

    private final PeriodRepository periodRepository;



    public Mono<ParametersResponse> apply(RequestParams requestParams, Map<String, String> header) {

        return periodRepository.getSelectedPeriodByUser(header.get(Constans.USER.getValue()))
                .collectList()
                .flatMap(periodsSelected -> {
                    if (periodsSelected.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }

                    Period periodSel = periodsSelected.get(0);

                    return baseScenarioRepository.getAllBetweenDates(periodSel.getInitialPeriod(),
                                    periodSel.getFinalPeriod())
                            .collectList()
                            .filter(o -> !o.isEmpty())
                            .flatMap(periods -> {
                                log.info("periods: " + periods);
                                return minimumRepository.getAllByPeriod(periods.stream().map(BasePeriodScenario::getPeriod).collect(Collectors.toList()))
                                        .filter(dpc -> {
                                            log.info("maximum daily: " + dpc.toString());
                                            return dpc.getName() != null;
                                        })
                                        .groupBy(MinimumDailyProductiveCapacity::getName)
                                        .flatMap(groupedFlux -> groupedFlux.collectList().map(dailyProductiveCapacities -> {
                                            Map<LocalDate, BasePeriodScenario> map = periods.stream()
                                                    .collect(Collectors.toMap(BasePeriodScenario::getPeriod, Function.identity()));
                                            MinimumCapacity minimumCapacity = new MinimumCapacity();
                                            minimumCapacity.setName(groupedFlux.key());
                                            List<MinimumCapacityValue> capacities = dailyProductiveCapacities.stream()
                                                    .map(dpc -> {
                                                        BigDecimal totalCapacity = BigDecimal.ZERO;
                                                        if (map.containsKey(dpc.getPeriod())) {
                                                            BasePeriodScenario period = map.get(dpc.getPeriod());
                                                            totalCapacity = calculateTotalCapacity(dpc.getMaximumCapacity(), period.getWorkDays(), period.getExtraDays());
                                                        }
                                                        return new MinimumCapacityValue(dpc.getPeriod(), totalCapacity);
                                                    })
                                                    .filter(Objects::nonNull)
                                                    .collect(Collectors.toList());
                                            minimumCapacity.setCapacity(capacities);
                                            return minimumCapacity;
                                        }))
                                        .collectList()
                                        .map(maximumCapacities ->  ParametersResponse.of(maximumCapacities, Long.valueOf(maximumCapacities.size())));
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
