package cl.camanchaca.business.usecases.optimization;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.optimization.DownloadDemandPeriodRepository;
import cl.camanchaca.domain.models.Period;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class DownloadDemandPeriodExcelUseCase {

    private final DownloadDemandPeriodRepository demandPeriodRepository;
    private final GetSelectedPeriodUseCase getSelectedPeriodUseCase;
    private final GetAllDemandPeriodUseCase getAllDemandPeriodUseCase;
    private final PeriodRepository periodRepository;

    public Mono<byte[]> apply(Map<String, String> header) {

        return periodRepository.getSelectedPeriodByUser(header.get(Constans.USER.getValue()))
                .collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.just(new byte[0]);
                    }

                    Period period = periods.get(0);
                    return demandPeriodRepository.dowloadPeriodExcel(getAllDemandPeriodUseCase.getAll(period.getInitialPeriod(), period.getFinalPeriod()), getSelectedPeriodUseCase.getSelectedMonths(header));
                });

    }

}
