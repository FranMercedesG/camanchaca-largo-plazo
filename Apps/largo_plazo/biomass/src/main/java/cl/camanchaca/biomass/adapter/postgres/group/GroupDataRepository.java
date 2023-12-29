package cl.camanchaca.biomass.adapter.postgres.group;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroupDataRepository extends ReactiveCrudRepository<GroupData, UUID> {
}
