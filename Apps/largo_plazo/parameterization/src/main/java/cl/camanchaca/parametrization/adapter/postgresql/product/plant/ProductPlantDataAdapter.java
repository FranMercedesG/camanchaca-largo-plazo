package cl.camanchaca.parametrization.adapter.postgresql.product.plant;

import cl.camanchaca.business.repositories.ProductPlantRepository;
import cl.camanchaca.domain.dtos.ParameterPlantDTO;
import cl.camanchaca.parametrization.mappers.ProductPlantMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Service
@AllArgsConstructor
public class ProductPlantDataAdapter implements ProductPlantRepository {

    private final ProductPlantDataRepository productPlantDataRepository;

    @Override
    public Flux<ParameterPlantDTO> saveAll(Flux<ParameterPlantDTO> parameters) {
        return parameters.map(ProductPlantMapper::toProductPlantData)
                .collectList()
                .flatMapMany(productPlantDataRepository::saveAll)
                .map(ProductPlantMapper::toProductPlant);
    }

    @Override
    public Flux<ParameterPlantDTO> getByPlantId(UUID plantId) {
        return productPlantDataRepository.findByPlantId(plantId)
                .map(ProductPlantMapper::toProductPlant);
    }

    @Override
    public Flux<ParameterPlantDTO> getByProducttId(Integer productId) {
        return productPlantDataRepository.findByProductId(productId)
                .map(ProductPlantMapper::toProductPlant);
    }

    @Override
    public Flux<ParameterPlantDTO> getByProducttIdAndPlantId(Integer productId, UUID plantId) {
        return productPlantDataRepository.findByProductIdAndPlantId(productId,plantId)
                .map(ProductPlantMapper::toProductPlant);
    }

    @Override
    public Mono<Long> countByPlantId(UUID plantId) {
        return productPlantDataRepository.countByPlantId(plantId);
    }
}
