package cl.camanchaca.parametrization.adapter.postgresql.product.group;

import cl.camanchaca.business.repositories.ParameterGroupRepository;
import cl.camanchaca.domain.dtos.ParameterGroupDTO;
import cl.camanchaca.generics.errors.InfraestructureException;
import cl.camanchaca.parametrization.adapter.postgresql.group.GroupDataRepository;
import cl.camanchaca.parametrization.mappers.ProductGroupMapper;
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

    private final GroupDataRepository groupDataRepository;

    @Override
    public Flux<ParameterGroupDTO> getByProductCode(Integer codigo) {
        return productGroupDataRepository.findAllByProductId(codigo).map(ProductGroupMapper::toParameterGroupDTO);
    }

    @Override
    public Flux<ParameterGroupDTO> getAll() {
        return productGroupDataRepository.findAll()
                .map(ProductGroupMapper::toParameterGroupDTO);
    }

    @Override
    public Flux<ParameterGroupDTO> getByProductIdAndGroupId(Integer productId, UUID groudId) {
        return productGroupDataRepository.findByProductIdAndGroupId(productId, groudId)
                .map(ProductGroupMapper::toParameterGroupDTO);
    }

    @Override
    public Mono<ParameterGroupDTO> save(ParameterGroupDTO parameterGroupDTO) {
        return productGroupDataRepository.save(
                        ProductGroupMapper.toProductGroupData(parameterGroupDTO)
                )
                .map(ProductGroupMapper::toParameterGroupDTO);
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
        return Flux.fromIterable(data).flatMap(this::saveParameterAndGroup);
    }

    @Override
    public Mono<ParameterGroupDTO> saveParameterAndGroup(ParameterGroupDTO parameterGroupDTO) {
        return Mono.defer(() -> {
                    UUID groupId = parameterGroupDTO.getGroupId();

                    if (groupId == null) {
                        return groupDataRepository.insertGroup(ProductGroupMapper.toGroupData(parameterGroupDTO))
                                .flatMap(insertedGroup -> {
                                    parameterGroupDTO.setGroupId(insertedGroup.getGroupId());
                                    parameterGroupDTO.setGroupName(insertedGroup.getGroupName());
                                    return Mono.just(parameterGroupDTO);
                                });
                    } else {
                        return groupDataRepository.existsById(groupId)
                                .flatMap(exists -> {
                                    if (Boolean.TRUE.equals(exists)) {
                                        return groupDataRepository.save(ProductGroupMapper.toGroupData(parameterGroupDTO))
                                                .thenReturn(parameterGroupDTO);
                                    } else {
                                        return Mono.error(new InfraestructureException("No se encontró el grupo para actualizar"));
                                    }
                                });
                    }

                }).then(
                        productGroupDataRepository.save(ProductGroupMapper.toProductGroupData(parameterGroupDTO))
                )
                .map(ProductGroupMapper::toParameterGroupDTO);


    }

    @Override
    public Mono<Void> deleteGroupById(UUID uuid) {
        return groupDataRepository.deleteById(uuid);
    }

}
