package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.repositories.DownloadActiveOptimizationRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.business.repositories.optimization.OptimizationRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.optimization.Optimization;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class DownloadActiveExcelUseCase  {

    private final OptimizationRepository optimizationRepository;
    private final SizeRepository sizeRepository;
    private final DownloadActiveOptimizationRepository downloadActiveOptimizationRepository;
    private final PeriodRepository periodRepository;

    public Mono<byte[]> apply(Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get("user"))
                .collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.empty();
                    }

                    Period period = periods.get(0);

                    return downloadActiveOptimizationRepository.downloadActiveOptimization(getData(period.getInitialPeriod(), period.getFinalPeriod()));
                });
    }


    private Flux<Optimization> getData(LocalDate initialMonth, LocalDate finalMonth){
        return optimizationRepository
                .getActiveByInitialAndFinalMonth(initialMonth, finalMonth)
                .flatMap(optimization -> sizeRepository
                        .getById(optimization.getSizeId())
                        .map(size -> optimization.toBuilder()
                                .sizeName(size.nameColunm())
                                .build()));
    }
}
