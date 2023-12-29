package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.business.repositories.optimization.OptimizationRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.optimization.Optimization;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

@AllArgsConstructor
public class GetActiveOptimizationByMonthUseCase {

    private final OptimizationRepository optimizationRepository;
    private final SizeRepository sizeRepository;
    private final PeriodRepository periodRepository;

    public Flux<Optimization> apply(Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get("user"))
                .collectList()
                .flatMapMany(periods -> {
                    if (periods.isEmpty()) {
                        return Flux.empty();
                    }

                    Period period = periods.get(0);

                    return optimizationRepository
                            .getActiveByMonth(period.getInitialPeriod(), period.getFinalPeriod())
                            .concatMap(optimization -> sizeRepository
                                    .getById(optimization.getSizeId())
                                    .map(size -> optimization.toBuilder()
                                            .sizeName(size.nameColunm())
                                            .build()));
                });
    }
}
