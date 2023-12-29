package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DownloadAdminRepository {
    Mono<byte[]> downloadAdminFile(Flux<UnrestrictedDemand> demands);

}
