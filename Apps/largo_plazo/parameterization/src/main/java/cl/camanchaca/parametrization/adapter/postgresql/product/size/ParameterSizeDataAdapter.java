package cl.camanchaca.parametrization.adapter.postgresql.product.size;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.repositories.ParameterSizeRepository;
import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import cl.camanchaca.generics.errors.InfraestructureException;
import cl.camanchaca.parametrization.mappers.ProductSizeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ParameterSizeDataAdapter implements ParameterSizeRepository {

    private final ProductSizeDataRepository productSizeDataRepository;

    @Override
    public Flux<ParameterSizePerformanceDTO> getAll() {
        return productSizeDataRepository.findAll()
                .map(ProductSizeMapper::toParameterSize);

    }

    @Override
    public Mono<ParameterSizePerformanceDTO> getById(UUID id) {
        return productSizeDataRepository.findById(id)
                .map(ProductSizeMapper::toParameterSize);
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> getByPlantIdAndProductId(UUID plantId, Integer productId) {
        return Flux.error(new InfraestructureException(Constans.NO_IMPLEMENT_METHOD.getValue()));
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> getByPlantIdAndProductIdAndSizeId(UUID plantId, Integer productId, UUID sizeId) {
        return Flux.error(new InfraestructureException(Constans.NO_IMPLEMENT_METHOD.getValue()));
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> saveAll(Flux<ParameterSizePerformanceDTO> performances) {
        return productSizeDataRepository.saveAll(
                        performances
                                .map(ProductSizeMapper::toParameterSizeData)
                )
                .map(ProductSizeMapper::toParameterSize);
    }

    @Override
    public Mono<ParameterSizePerformanceDTO> save(ParameterSizePerformanceDTO performances) {
        return productSizeDataRepository.save(
                        ProductSizeMapper.toParameterSizeData(performances)
                )
                .map(ProductSizeMapper::toParameterSize);
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> getByPageAndSizeAndPlantId(Integer page, Integer size, UUID plantId) {
        return Flux.error(new InfraestructureException(Constans.NO_IMPLEMENT_METHOD.getValue()));
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> getByProductIdAndSizeId(Integer productId, UUID sizeId) {
        return productSizeDataRepository.findByProductIdAndAndSizeId(productId, sizeId)
                .map(ProductSizeMapper::toParameterSize);
    }
    public Flux<ParameterSizePerformanceDTO> getAllByProductIdIn(Flux<Integer> ids) {
        return productSizeDataRepository.getAllByProductIdIn(ids)
                .map(ProductSizeMapper::toParameterSize);
    }

    @Override
    public Mono<Long> count() {
        return productSizeDataRepository.count();
    }

    @Override
    public Mono<Long> countByPlant(UUID plantId) {
        return Mono.error(new InfraestructureException(Constans.NO_IMPLEMENT_METHOD.getValue()));
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> getByProductCode(Integer codigo) {
        return productSizeDataRepository.findAllByProductId(codigo).map(ProductSizeMapper::toParameterSize);
    }
}
