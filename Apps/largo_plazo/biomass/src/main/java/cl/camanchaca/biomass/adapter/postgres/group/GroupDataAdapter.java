package cl.camanchaca.biomass.adapter.postgres.group;

import cl.camanchaca.biomass.mappers.postgres.GroupMapper;
import cl.camanchaca.business.repositories.GroupRepository;
import cl.camanchaca.domain.models.Group;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
@Service
@AllArgsConstructor
public class GroupDataAdapter implements GroupRepository {

    private final GroupDataRepository groupDataRepository;
    @Override
    public Flux<Group> getAll() {
        return groupDataRepository.findAll().map(GroupMapper::toGroup);
    }
}
