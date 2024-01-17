package cl.camanchaca.parametrization.adapter.postgresql.product.minimum;

import cl.camanchaca.business.repositories.ParameterMinimumRepository;
import cl.camanchaca.domain.dtos.ParameterMinimumDTO;
import cl.camanchaca.parametrization.mappers.ProductMinimumMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductMinimumAdapter implements ParameterMinimumRepository {

    private final ProductMinimumDataRepository productMinimumDataRepository;
    @Override
    public Flux<ParameterMinimumDTO> getByProductCode(Integer codigo) {
        return productMinimumDataRepository.findAllByProductId(codigo).map(ProductMinimumMapper::toParameterMinimumDTO);
    }

    @Override
    public Flux<ParameterMinimumDTO> getAll() {
        return productMinimumDataRepository.findAll().map(ProductMinimumMapper::toParameterMinimumDTO);
    }

    @Override
    public Flux<ParameterMinimumDTO> getByProductIdAndSizeId(Integer productId, UUID minimumId) {
        return productMinimumDataRepository.findByProductIdAndMinimumId(productId,minimumId)
                .map(ProductMinimumMapper::toParameterMinimumDTO);
    }

    @Override
    public Mono<ParameterMinimumDTO> save(ParameterMinimumDTO parameterMinimumDTO) {
        return productMinimumDataRepository.save(
                ProductMinimumMapper.toProductMinimumData(parameterMinimumDTO)
        )
                .map(ProductMinimumMapper::toParameterMinimumDTO);
    }
}
