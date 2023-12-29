package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.ProductiveCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumPeriodDailyProductiveCapacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MaximumCapacityRepository {
    Flux<MaximumDailyProductiveCapacity> getAll();

    Flux<MaximumDailyProductiveCapacity> getAllByPeriod(List<LocalDate> period);

    Mono<UUID> saveProductiveCapacity(String name);

    Flux<ProductiveCapacity> getAllProductiveCapacity();

    Mono<UUID> savePeriodDailyCapacity(MaximumPeriodDailyProductiveCapacity body);

    Flux<UUID> saveAllPeriodDailyProductiveCapacity(List<MaximumPeriodDailyProductiveCapacity> body);

    Mono<Void> deleteAllPeriodDailyProductiveCapacityByUUID(List<UUID> collect);

    Mono<Void> deleteAllProductiveCapacityByUUID(List<UUID> collect);

}
