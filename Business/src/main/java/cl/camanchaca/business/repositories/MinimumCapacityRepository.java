package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.Minimum;
import cl.camanchaca.domain.models.capacity.maximum.MaximumPeriodDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumPeriodDailyProductiveCapacity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface MinimumCapacityRepository {
    Flux<Minimum> getAll();

    Flux<MinimumDailyProductiveCapacity> getAllByPeriod(List<LocalDate> periods);

    Mono<Void> deleteAllPeriodDailyCapacityByUUID(List<UUID> collect);

    Mono<Void> deleteAllProductiveCapacityByUUID(List<UUID> collect);

    Mono<UUID> saveProductiveCapacity(String name);

    Flux<UUID> saveAllPeriodDailyCapacity(List<MinimumPeriodDailyProductiveCapacity> collect);

    Flux<Minimum> getAllProductiveCapacity();
}

