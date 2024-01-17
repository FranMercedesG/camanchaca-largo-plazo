package cl.camanchaca.biomass.adapter.postgres.groupsizemax;

import cl.camanchaca.business.repositories.biomass.GroupSizeMaxRepository;
import cl.camanchaca.domain.models.biomass.GroupSizeMax;
import cl.camanchaca.generics.errors.InfraestructureException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GroupMaxSizeDataAdapter implements GroupSizeMaxRepository {

    private final GroupMaxSizeDataRepository groupDataRepository;

    @Override
    public Flux<GroupSizeMax> getGroupSizeMaxByPeriodIN(LocalDate initialPeriod, LocalDate finalPeriod, List<UUID> groupsUUID) {
        return groupDataRepository.findGroupMaxSizeDataByPeriodAndGroups(initialPeriod, finalPeriod, groupsUUID)
                .map(o -> GroupSizeMax
                        .builder()
                        .groupSizeMaxId(o.getMaxSizeGroupId())
                        .period(o.getPeriod())
                        .groupId(o.getGroupId())
                        .sizeId(o.getSizeId())
                        .maxLimit(o.getMaximumLimit())
                        .build());
    }

    @Override
    public Mono<Void> deleteById(UUID uuid) {
        return groupDataRepository.deleteById(uuid);
    }

    @Override
    public Flux<GroupSizeMax> saveAll(List<GroupSizeMax> data) {
        return Flux.fromIterable(data).flatMap(this::saveGroupSize);
    }

    @Override
    public Mono<GroupSizeMax> saveGroupSize(GroupSizeMax groupSizeMax) {
        return Mono.defer(() -> {
            UUID groupSizeMaxId = groupSizeMax.getGroupSizeMaxId();

            if (groupSizeMaxId == null) {
                return groupDataRepository.insertData(toGroupSizeMaxData(groupSizeMax))
                        .flatMap(insertedGroupSizeMax ->  Mono.just(groupSizeMax));
            } else {
                return groupDataRepository.existsById(groupSizeMaxId)
                        .flatMap(exists -> {
                            if (Boolean.TRUE.equals(exists)) {
                                return groupDataRepository.save(toGroupSizeMaxData(groupSizeMax))
                                        .thenReturn(groupSizeMax);
                            } else {
                                return Mono.error(new InfraestructureException("No se encontró el grupo para actualizar"));
                            }
                        });
            }
        });
    }




    private GroupMaxSizeData toGroupSizeMaxData(GroupSizeMax data) {
        UUID maxSizeGroupId = (data != null && data.getGroupSizeMaxId() != null) ? data.getGroupSizeMaxId() : null;

        return GroupMaxSizeData
                .builder()
                .maxSizeGroupId(maxSizeGroupId)
                .groupId(data.getGroupId())
                .sizeId(data.getSizeId())
                .period(data.getPeriod())
                .maximumLimit(data.getMaxLimit())
                .build();
    }
}
