package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.capacity.minimum.MinimumTotalProductiveCapacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.*;

public interface TotalMinimumRepository {
    Flux<MinimumTotalProductiveCapacity> saveAllTotalProductive(List<MinimumTotalProductiveCapacity> total);
    Mono<Void> deleteAll();

    Flux<MinimumTotalProductiveCapacity> getAllTotalProductiveByPeriod(List<LocalDate> collect);
}
