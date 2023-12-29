package cl.camanchaca.biomass.adapter.postgres.product.group;

import cl.camanchaca.biomass.mappers.ProductGroupMapper;
import cl.camanchaca.business.repositories.ParameterGroupRepository;
import cl.camanchaca.domain.dtos.ParameterGroupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductGroupAdapter implements ParameterGroupRepository {

    private final ProductGroupDataRepository productGroupDataRepository;
    @Override
    public Flux<ParameterGroupDTO> getByProductCode(Integer codigo) {
        return productGroupDataRepository.findAllByProductId(codigo).map(ProductGroupMapper::toParameterGroupDTOp);
    }

    @Override
    public Flux<ParameterGroupDTO> getByProductIdAndGroupId(Integer productId, UUID groudId) {
        return productGroupDataRepository.findByProductIdAndGroupId(productId,groudId)
                .map(ProductGroupMapper::toParameterGroupDTOp);
    }

    @Override
    public Mono<ParameterGroupDTO> save(ParameterGroupDTO parameterGroupDTO) {
        return productGroupDataRepository.save(
                ProductGroupMapper.toProductGroupData(parameterGroupDTO)
                )
                .map(ProductGroupMapper::toParameterGroupDTOp);
    }

    @Override
    public Mono<Void> deleteById(UUID uuid) {
        return productGroupDataRepository.deleteById(uuid);
    }

    @Override
    public Mono<Void> deleteAll() {
        return productGroupDataRepository.deleteAll();
    }

    @Override
    public Flux<ParameterGroupDTO> saveAll(List<ParameterGroupDTO> data) {
        return Flux.fromIterable(data).flatMap(this::save);
    }

    @Override
    public Flux<ParameterGroupDTO> saveAllParameterAndGroup(List<ParameterGroupDTO> data) {
        return null;
    }

    @Override
    public Mono<ParameterGroupDTO> saveParameterAndGroup(ParameterGroupDTO parameterGroupDTO) {
        return null;
    }

    @Override
    public Mono<Void> deleteGroupById(UUID uuid) {
        return null;
    }
}
