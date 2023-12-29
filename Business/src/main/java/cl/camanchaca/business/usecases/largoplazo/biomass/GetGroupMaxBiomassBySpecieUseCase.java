package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.repositories.*;
import cl.camanchaca.business.repositories.biomass.GroupSizeMaxRepository;
import cl.camanchaca.business.responses.ProjectedBiomassResponse;
import cl.camanchaca.business.utils.SizeUtils;
import cl.camanchaca.domain.models.Group;
import cl.camanchaca.domain.models.Period;
import cl.camanchaca.domain.models.Size;
import cl.camanchaca.domain.models.biomass.GroupSizeMax;
import cl.camanchaca.domain.models.biomass.GroupSizeMaxValue;
import cl.camanchaca.domain.models.biomass.GroupSizeMaximum;
import cl.camanchaca.domain.models.biomass.GroupSizeMaximumDTO;
import cl.camanchaca.domain.models.parameters.ParameterGroup;
import cl.camanchaca.domain.models.product.ProductGroup;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetGroupMaxBiomassBySpecieUseCase {

    private final ProductRepository productRepository;
    private final ParameterGroupRepository parameterGroupRepository;
    private final GroupRepository groupRepository;
    private final GroupSizeMaxRepository groupSizeMaxRepository;
    private final PeriodRepository periodRepository;
    private final SizeRepository sizeRepository;

    public Mono<ParametersResponse> apply(RequestParams params, Map<String, String> header) {
        return periodRepository.getSelectedPeriodByUser(header.get("user"))
                .collectList()
                .flatMap(periods -> {
                    if (periods.isEmpty()) {
                        return Mono.just(ParametersResponse.of(Collections.emptyList(), 0L));
                    }

                    Period period = periods.get(0);
                    return getUUIDsFromProductGroups(params.getPage(), params.getSize(), params.getSpecie(), params.getGroupId())
                            .collectList()
                            .flatMapMany(groupsUUID -> {
                                        if (groupsUUID.isEmpty()) {
                                            return Flux.empty();
                                        }
                                        return groupSizeMaxRepository
                                                .getGroupSizeMaxByPeriodIN(period.getInitialPeriod(), period.getFinalPeriod(), groupsUUID);
                                    }
                            )
                            .collectList()
                            .flatMap(groupSizeMaxList -> {
                                return processGroupSizeMax(groupSizeMaxList, params.getSpecie())
                                        .flatMap(processedGroupSizeMaxList -> {
                                            return Mono.just(ParametersResponse.of(processedGroupSizeMaxList, Long.valueOf(processedGroupSizeMaxList.size())));
                                        });
                            });
                });
    }

    public Mono<List<GroupSizeMaximumDTO>> processGroupSizeMax(List<GroupSizeMax> groupSizeMaxList, String specie) {
        return Flux.fromIterable(groupSizeMaxList)
                .groupBy(groupSizeMax -> Arrays.asList(groupSizeMax.getGroupId(), groupSizeMax.getSizeId()))
                .flatMap(groupedFlux -> {
                    UUID groupId = groupedFlux.key().get(0);
                    UUID sizeId = groupedFlux.key().get(1);

                    Mono<String> groupNameMono = groupRepository.getAll()
                            .collectMap(Group::getId, Group::getName)
                            .map(groupMap -> groupMap.getOrDefault(groupId, "N/A"));

                    Mono<String> sizeName = sizeRepository.getBySpecie(specie)
                            .collectMap(Size::getId, size -> size.getInitialRange() + "-" + size.getFinalRange() + " " + size.getUnit())
                            .map(sizeMap -> sizeMap.getOrDefault(sizeId, "N/A"));

                    Mono<String> qualityName = sizeRepository.getBySpecie(specie)
                            .collectMap(Size::getId, size -> size.getPieceType())
                            .map(sizeMap -> sizeMap.getOrDefault(sizeId, "N/A"));

                    return Mono.zip(groupNameMono, sizeName, qualityName)
                            .flatMap(tuple -> {
                                String groupName = tuple.getT1();
                                String sizeInfo = tuple.getT2();
                                String quality = tuple.getT3();

                                Mono<List<GroupSizeMax>> groupedMaxList = groupedFlux.collectList();
                                return groupedMaxList.map(maxList -> {
                                    List<GroupSizeMaxValue> values = maxList.stream()
                                            .map(groupSizeMax -> new GroupSizeMaxValue(groupSizeMax.getPeriod(), groupSizeMax.getMaxLimit()))
                                            .collect(Collectors.toList());

                                    UUID groupSizeMaxId = maxList.get(0).getGroupSizeMaxId();
                                    return new GroupSizeMaximumDTO(groupSizeMaxId, groupId, groupName,sizeId, sizeInfo, quality ,values);
                                });
                            });
                })
                .filter(groupSizeMaximumDTO -> !groupSizeMaximumDTO.getSizeName().equals("N/A"))
                .sort((group1, group2) -> SizeUtils.sortGroups().compare(group1, group2))
                .collectList();
    }




    public Flux<UUID> getUUIDsFromProductGroups(Integer page, Integer size, String specie, UUID id) {
        return getRequestData(page, size, specie, id)
                .flatMap(productGroup -> Flux.fromIterable(productGroup.getGroups())
                        .filter(parameterGroup -> parameterGroup.getColumnId().equals(id))
                        .map(ParameterGroup::getColumnId));
    }

    private Flux<ProductGroup> getRequestData(Integer page, Integer size, String specie, UUID id) {
        return productRepository.getByPageAndSize(page, size)
                .flatMap(product -> parameterGroupRepository.getByProductCode(product.getCodigo())
                        .collectList()
                        .map(parameterSizePerformanceDTOS -> ProductGroup.builder()
                                .productId(product.getCodigo())
                                .description(product.getDescripcion())
                                .species(product.getEspecie())
                                .groups(parameterSizePerformanceDTOS.stream()
                                        .filter(dto -> dto.getGroupId().equals(id))
                                        .map(dto -> ParameterGroup.builder()
                                                .id(dto.getId())
                                                .columnId(dto.getGroupId())
                                                .status(dto.getStatus())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build()))
                .filter(p -> p.getSpecies().equalsIgnoreCase(specie)
                        && p.getGroups().stream().anyMatch(group -> group.getColumnId().equals(id)));
    }

}
