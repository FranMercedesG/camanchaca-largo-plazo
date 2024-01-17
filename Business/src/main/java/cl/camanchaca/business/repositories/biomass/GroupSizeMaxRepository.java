package cl.camanchaca.business.repositories.biomass;

import cl.camanchaca.domain.models.biomass.GroupSizeMax;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GroupSizeMaxRepository {
    Flux<GroupSizeMax> getGroupSizeMaxByPeriodIN(LocalDate initialPeriod, LocalDate finalPeriod, List<UUID> groupsUUID);

    Mono<Void> deleteById(UUID uuid);

    Flux<GroupSizeMax> saveAll(List<GroupSizeMax> data);

    Mono<GroupSizeMax> saveGroupSize(GroupSizeMax groupSizeMax);
}
