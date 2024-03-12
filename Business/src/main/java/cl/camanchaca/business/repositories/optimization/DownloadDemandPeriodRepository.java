package cl.camanchaca.business.repositories.optimization;

import cl.camanchaca.domain.models.optimization.DemandPeriod;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface DownloadDemandPeriodRepository {
    Mono<byte[]> dowloadPeriodExcel(Flux<DemandPeriod> optimization, Mono<List<LocalDate>> dates);
}
