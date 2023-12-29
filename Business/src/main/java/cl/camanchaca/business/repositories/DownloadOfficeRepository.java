package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.demand.UnrestrictedDemandOffice;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface DownloadOfficeRepository {
    Mono<byte[]> downloadOfficeFile(Flux<UnrestrictedDemandOffice> demands,  Map<String, String> headerInfo);
}
