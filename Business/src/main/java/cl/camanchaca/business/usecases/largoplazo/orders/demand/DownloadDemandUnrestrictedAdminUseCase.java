package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.DownloadAdminRepository;
import cl.camanchaca.business.repositories.PeriodRepository;
import cl.camanchaca.business.repositories.UnrestrictedDemandRepository;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class DownloadDemandUnrestrictedAdminUseCase {

    private final UnrestrictedDemandRepository unrestrictedDemandRepository;

    private final PeriodRepository periodRepository;

    private final DownloadAdminRepository downloadAdminRepository;

    public Mono<byte[]> apply(RequestParams requestParams, Map<String, String> header) {
        return downloadAdminRepository.downloadAdminFile(getData(requestParams, header));
    }

    private Flux<UnrestrictedDemand> getData(RequestParams requestParams, Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get(Constans.USER.getValue()))
                .collectList()
                .flatMapMany(periods -> {
                    if (periods.isEmpty()) {
                        return Flux.empty();
                    }

                    Period period = periods.get(0);

                    return unrestrictedDemandRepository.getAllByPeriods(period.getInitialPeriod(), period.getFinalPeriod());
                });
    }


}
