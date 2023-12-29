package cl.camanchaca.business.repositories;

import cl.camanchaca.domain.models.Group;
import reactor.core.publisher.Flux;

public interface GroupRepository {
    Flux<Group> getAll();
}
