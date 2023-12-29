package cl.camanchaca.parametrization.adapter.postgresql.procutive.capacity;

import cl.camanchaca.business.repositories.ProductiveCapacityRepository;
import cl.camanchaca.domain.models.ProductiveCapacity;
import cl.camanchaca.parametrization.mappers.ProductiveCapacityMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@AllArgsConstructor
public class ProductiveCapacityDataAdapter implements ProductiveCapacityRepository {

    private final ProductiveCapacityDataRepository productiveCapacityDataRepository;

    @Override
    public Flux<ProductiveCapacity> getAll() {
        return productiveCapacityDataRepository.findAll()
                .map(ProductiveCapacityMapper::toProductiveCapacity);
    }
}
