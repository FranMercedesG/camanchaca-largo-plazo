package cl.camanchaca.parametrization.adapter.postgresql.group;

import cl.camanchaca.business.repositories.GroupRepository;
import cl.camanchaca.domain.models.Group;
import cl.camanchaca.parametrization.mappers.GroupMapper;
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
