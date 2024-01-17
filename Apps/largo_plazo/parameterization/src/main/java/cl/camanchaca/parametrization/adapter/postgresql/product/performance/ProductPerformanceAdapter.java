package cl.camanchaca.parametrization.adapter.postgresql.product.performance;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.repositories.ParameterPerformanceRepository;
import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import cl.camanchaca.domain.models.parameters.ParameterPerformance;
import cl.camanchaca.generics.errors.InfraestructureException;
import cl.camanchaca.parametrization.adapter.postgresql.product.size.ProductSizeDataRepository;
import cl.camanchaca.parametrization.mappers.ProductSizeMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductPerformanceAdapter implements ParameterPerformanceRepository {

    private final ProductSizeDataRepository productSizeDataRepository;


    @Override
    public Flux<ParameterSizePerformanceDTO> getAll() {
        return Flux.error(new InfraestructureException(Constans.NO_IMPLEMENT_METHOD.getValue()));
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> getByPlantIdAndProductId(UUID plantId, Integer productId) {
        return Flux.empty();
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> getByPlantIdAndProductIdAndSizeId(UUID plantId, Integer productId, UUID sizeId) {
        return Flux.empty();
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> getByPageAndSizeAndPlantId(Integer page, Integer size, UUID plantId) {
       return Flux.error(new InfraestructureException(Constans.NO_IMPLEMENT_METHOD.getValue()));
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> getByProductIdAndSizeId(Integer productId, UUID sizeId) {
        return productSizeDataRepository.findByProductIdAndAndSizeId(productId,sizeId)
                .map(ProductSizeMapper::toParameterSize);
    }

    @Override
    public Mono<ParameterSizePerformanceDTO> save(ParameterSizePerformanceDTO parameterSizePerformanceDTO) {
        return productSizeDataRepository.save(
                ProductSizeMapper.toParameterSizeData(parameterSizePerformanceDTO)
                )
                .map(ProductSizeMapper::toParameterSize);
    }

    @Override
    public Flux<ParameterSizePerformanceDTO> saveAll(Flux<ParameterPerformance> performances) {
        return Flux.error(new InfraestructureException(Constans.NO_IMPLEMENT_METHOD.getValue()));
    }

    @Override
    public Mono<Long> count() {
        return productSizeDataRepository.count();
    }
}
