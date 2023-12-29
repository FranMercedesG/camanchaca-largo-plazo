package cl.camanchaca.capacity.adapters.postgres.maximum;

import cl.camanchaca.business.repositories.MaximumCapacityRepository;
import cl.camanchaca.business.repositories.TotalMaximumRepository;
import cl.camanchaca.capacity.adapters.postgres.maximum.daily.MaximumPeriodDailyProductiveCapacityData;
import cl.camanchaca.capacity.adapters.postgres.maximum.daily.MaximumPeriodDailyProductiveCapacityDataRepository;
import cl.camanchaca.capacity.adapters.postgres.maximum.period.MaximumPeriodProductiveCapacityDataRepository;
import cl.camanchaca.capacity.adapters.postgres.maximum.productivecapacity.MaximumProductiveCapacityData;
import cl.camanchaca.capacity.adapters.postgres.maximum.productivecapacity.MaximumProductiveCapacityDataRepository;
import cl.camanchaca.capacity.mappers.PeriodDailyProductiveCapacityMapper;
import cl.camanchaca.capacity.mappers.ProductiveCapacityMapper;
import cl.camanchaca.domain.models.ProductiveCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumPeriodDailyProductiveCapacity;
import cl.camanchaca.domain.models.capacity.maximum.MaximumTotalProductiveCapacity;
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
public class MaximumProductiveCapacityDataAdapter implements MaximumCapacityRepository, TotalMaximumRepository {

    private final MaximumProductiveCapacityDataRepository productiveCapacityDataRepository;
    private final MaximumPeriodDailyProductiveCapacityDataRepository maximumRepository;
    private final MaximumPeriodProductiveCapacityDataRepository totalMaximumRepository;
    @Override
    public Flux<MaximumDailyProductiveCapacity> getAll() {
        return productiveCapacityDataRepository.findAllWithDailyCapacity().map(ProductiveCapacityMapper::toDailyProductiveCapacity);
    }

    @Override
    public Flux<MaximumDailyProductiveCapacity> getAllByPeriod(List<LocalDate> period) {
        return productiveCapacityDataRepository.findAllWithDailyCapacityByPeriods(period).map(ProductiveCapacityMapper::toDailyProductiveCapacity);
    }

    @Override
    public Mono<UUID> saveProductiveCapacity(String name) {
        return productiveCapacityDataRepository.save(MaximumProductiveCapacityData.builder().name(name).build()).map(MaximumProductiveCapacityData::getId);
    }

    @Override
    public Flux<ProductiveCapacity> getAllProductiveCapacity() {
        return productiveCapacityDataRepository
                .findAll()
                .map(ProductiveCapacityMapper::toProductiveCapacity);
    }

    @Override
    public Mono<UUID> savePeriodDailyCapacity(MaximumPeriodDailyProductiveCapacity body) {
        return maximumRepository.save(PeriodDailyProductiveCapacityMapper.toPeriodDailyProductiveCapacityData(body)).map(MaximumPeriodDailyProductiveCapacityData::getId);
    }

    @Override
    public Flux<UUID> saveAllPeriodDailyProductiveCapacity(List<MaximumPeriodDailyProductiveCapacity> body) {
        return maximumRepository
                .saveAll(body.stream()
                        .map(PeriodDailyProductiveCapacityMapper::toPeriodDailyProductiveCapacityData)
                        .collect(Collectors.toList())
                ).map(MaximumPeriodDailyProductiveCapacityData::getId);
    }

    @Override
    public Mono<Void> deleteAllPeriodDailyProductiveCapacityByUUID(List<UUID> collect) {
        if(collect.isEmpty()){
            return Mono.empty().then();
        }
        return maximumRepository.deleteAllByIdIn(collect);
    }

    @Override
    public Mono<Void> deleteAllProductiveCapacityByUUID(List<UUID> collect) {
        if(collect.isEmpty()){
            return Mono.empty().then();
        }
        return productiveCapacityDataRepository.deleteAllByIdIn(collect);
    }

    @Override
    public Mono<Void> deleteAll() {
        return totalMaximumRepository.deleteAll();
    }

    @Override
    public Flux<MaximumTotalProductiveCapacity> saveAllTotalProductive(List<MaximumTotalProductiveCapacity> maximum) {
        return totalMaximumRepository.saveAll(maximum.stream().map(ProductiveCapacityMapper::toPeriodCapacityData).collect(Collectors.toList())).map(ProductiveCapacityMapper::toTotalProductiveCapacity);
    }

    @Override
    public Flux<MaximumTotalProductiveCapacity> getAllTotalProductive() {
        return productiveCapacityDataRepository.findAllTotalProductives();
    }

    @Override
    public Flux<MaximumTotalProductiveCapacity> getAllTotalProductiveByPeriod(List<LocalDate> dates) {
        return productiveCapacityDataRepository.findAllTotalProductivesByPeriod(dates);
    }
}
