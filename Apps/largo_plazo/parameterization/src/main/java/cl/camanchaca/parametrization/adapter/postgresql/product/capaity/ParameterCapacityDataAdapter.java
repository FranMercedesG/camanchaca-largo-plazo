package cl.camanchaca.parametrization.adapter.postgresql.product.capaity;

import cl.camanchaca.business.repositories.ParameterCapacityRepository;
import cl.camanchaca.domain.dtos.ParameterCapacityDTO;
import cl.camanchaca.parametrization.mappers.ProductCapacityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ParameterCapacityDataAdapter implements ParameterCapacityRepository {

    private final ProductCapacityDataRepository productCapacityDataRepository;

    @Override
    public Flux<ParameterCapacityDTO> getAll() {
        return productCapacityDataRepository.findAll()
                .map(ProductCapacityMapper::toCapacity);
    }

    @Override
    public Flux<ParameterCapacityDTO> getByPlantIdAndProductId(UUID plantId, Integer productId) {
        return Flux.empty();
    }

    @Override
    public Flux<ParameterCapacityDTO> getByPlantIdAndProductIdAndProductiveCapacityIdId(UUID plantId, Integer productId, UUID productiveCapacityId) {
        return Flux.empty();
    }

    @Override
    public Flux<ParameterCapacityDTO> saveAll(Flux<ParameterCapacityDTO> capacities) {
        return productCapacityDataRepository.saveAll(
                capacities
                        .map(ProductCapacityMapper::toProductCapacityData)
        ).map(ProductCapacityMapper::toCapacity);
    }

    @Override
    public Flux<ParameterCapacityDTO> getByPageAndSizeAndPlantId(Integer page, Integer size, UUID plantId) {
        return Flux.empty();
    }

    @Override
    public Flux<ParameterCapacityDTO> getByPageAndSize(Integer page, Integer size) {
        return productCapacityDataRepository.findAllByOffsetAndSize(page, size)
                .map(ProductCapacityMapper::toCapacity);
    }

    @Override
    public Mono<Long> count() {
        return productCapacityDataRepository.count();
    }

    @Override
    public Mono<Long> countByPlantId(UUID plantId) {
        return Mono.empty();
    }

    @Override
    public Mono<ParameterCapacityDTO> save(ParameterCapacityDTO parameterCapacityDTO) {
        return productCapacityDataRepository.save(
                        ProductCapacityMapper.toProductCapacityData(parameterCapacityDTO)
                )
                .map(ProductCapacityMapper::toCapacity);
    }

    @Override
    public Flux<ParameterCapacityDTO> getByProductIdAndProductiveCapacityId(Integer productId, UUID productiveCapacityId) {
        return productCapacityDataRepository.findByProductIdAndAndProductiveCapacityId(productId, productiveCapacityId)
                .map(ProductCapacityMapper::toCapacity);
    }

    @Override
    public Flux<ParameterCapacityDTO> getByProductCode(Integer codigo) {
        return productCapacityDataRepository.findAllByProductId(codigo).map(ProductCapacityMapper::toCapacity);
    }

}
