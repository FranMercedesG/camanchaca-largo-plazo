package cl.camanchaca.capacity.adapters.postgres.minimum;

import cl.camanchaca.business.repositories.MinimumCapacityRepository;
import cl.camanchaca.business.repositories.TotalMinimumRepository;
import cl.camanchaca.capacity.adapters.postgres.minimum.daily.MinimumPeriodDailyProductiveCapacityData;
import cl.camanchaca.capacity.adapters.postgres.minimum.daily.MinimumPeriodDailyProductiveDataRepository;
import cl.camanchaca.capacity.adapters.postgres.minimum.minimumcapacity.MinimumData;
import cl.camanchaca.capacity.adapters.postgres.minimum.minimumcapacity.MinimumDataRepository;
import cl.camanchaca.capacity.adapters.postgres.minimum.period.MinimumPeriodProductiveCapacityDataRepository;
import cl.camanchaca.capacity.mappers.MinimumProductiveMapper;
import cl.camanchaca.domain.models.Minimum;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumPeriodDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.minimum.MinimumTotalProductiveCapacity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MinimumProductiveCapacityDataAdapter implements MinimumCapacityRepository, TotalMinimumRepository {

    private final MinimumDataRepository minimumDataRepository;
    private final MinimumPeriodDailyProductiveDataRepository minimumPeriodRepository;
    private final MinimumPeriodProductiveCapacityDataRepository totalRepository;
    @Override
    public Flux<Minimum> getAll() {
        return Flux.empty();
    }

    @Override
    public Flux<MinimumDailyProductiveCapacity> getAllByPeriod(List<LocalDate> periods) {
       return minimumDataRepository.findAllWithDailyCapacityByPeriods(periods).map(MinimumProductiveMapper::toDailyProductiveCapacity);
    }

    @Override
    public Mono<Void> deleteAllPeriodDailyCapacityByUUID(List<UUID> collect) {
        if(collect.isEmpty()){
            return Mono.empty().then();
        }
        return minimumPeriodRepository.deleteAllByIdIn(collect);
    }

    @Override
    public Mono<Void> deleteAllProductiveCapacityByUUID(List<UUID> collect) {
        if(collect.isEmpty()){
            return Mono.empty().then();
        }
        return minimumDataRepository.deleteAllByIdIn(collect);
    }

    @Override
    public Mono<UUID> saveProductiveCapacity(String name) {
        return minimumDataRepository.save(MinimumData.builder().name(name).build()).map(MinimumData::getMinimumId);
    }

    @Override
    public Flux<UUID> saveAllPeriodDailyCapacity(List<MinimumPeriodDailyProductiveCapacity> collect) {
        return minimumPeriodRepository
                .saveAll( collect.stream()
                        .map(MinimumProductiveMapper::toPeriodDailyData)
                        .collect(Collectors.toList())
                )
                .map(MinimumPeriodDailyProductiveCapacityData::getId);
    }

    @Override
    public Flux<Minimum> getAllProductiveCapacity() {
        return minimumDataRepository.findAll().map(MinimumProductiveMapper::toPeriodProductiveCapacity);
    }

    @Override
    public Flux<MinimumTotalProductiveCapacity> saveAllTotalProductive(List<MinimumTotalProductiveCapacity> total) {
        return totalRepository.saveAll(total.stream().map(MinimumProductiveMapper::toPeriodProductiveCapacity).collect(Collectors.toList())).map(MinimumProductiveMapper::toPeriodProductiveCapacity);
    }

    @Override
    public Mono<Void> deleteAll() {
        return totalRepository.deleteAll();
    }

    @Override
    public Flux<MinimumTotalProductiveCapacity> getAllTotalProductiveByPeriod(List<LocalDate> collect) {
        return minimumDataRepository.findAllTotalProductivesByPeriod(collect);
    }
}
