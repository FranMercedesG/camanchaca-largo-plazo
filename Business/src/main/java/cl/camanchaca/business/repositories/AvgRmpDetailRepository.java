package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.demand.RmpDetail;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface AvgRmpDetailRepository {
    Flux<RmpDetail> getAllById(UUID id);
}
