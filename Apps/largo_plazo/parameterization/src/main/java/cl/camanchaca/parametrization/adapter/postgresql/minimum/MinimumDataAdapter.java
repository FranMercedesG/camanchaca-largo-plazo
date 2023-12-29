package cl.camanchaca.parametrization.adapter.postgresql.minimum;

import cl.camanchaca.business.repositories.MinimumCapacityRepository;
import cl.camanchaca.domain.models.Minimum;
import cl.camanchaca.domain.models.capacity.minimum.MinimumDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumPeriodDailyProductiveCapacity;
import cl.camanchaca.parametrization.mappers.MinimumMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class MinimumDataAdapter implements MinimumCapacityRepository {

    private final MinimumDataRepository minimumDataRepository;
    @Override
    public Flux<Minimum> getAll() {
        return minimumDataRepository.findAll().map(MinimumMapper::toMinimum);
    }

    @Override
    public Flux<MinimumDailyProductiveCapacity> getAllByPeriod(List<LocalDate> periods) {
        return null;
    }

    @Override
    public Mono<Void> deleteAllPeriodDailyCapacityByUUID(List<UUID> collect) {
        return null;
    }

    @Override
    public Mono<Void> deleteAllProductiveCapacityByUUID(List<UUID> collect) {
        return null;
    }

    @Override
    public Mono<UUID> saveProductiveCapacity(String name) {
        return null;
    }

    @Override
    public Flux<UUID> saveAllPeriodDailyCapacity(List<MinimumPeriodDailyProductiveCapacity> collect) {
        return null;
    }

    @Override
    public Flux<Minimum> getAllProductiveCapacity() {
        return null;
    }

}
