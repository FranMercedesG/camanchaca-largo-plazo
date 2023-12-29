package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.capacity.maximum.MaximumTotalProductiveCapacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

public interface TotalMaximumRepository {

    Mono<Void> deleteAll();
    Flux<MaximumTotalProductiveCapacity> saveAllTotalProductive(List<MaximumTotalProductiveCapacity> maximum);

    Flux<MaximumTotalProductiveCapacity> getAllTotalProductive();

    Flux<MaximumTotalProductiveCapacity> getAllTotalProductiveByPeriod(List<LocalDate> dates);
}
