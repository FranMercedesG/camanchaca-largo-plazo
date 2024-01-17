package cl.camanchaca.parametrization.adapter.postgresql.group;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

@Repository
public interface GroupDataRepository extends ReactiveCrudRepository<GroupData, UUID> {
    Mono<GroupData> findByGroupIdAndGroupName(UUID groupId ,String groupName);

    @Query("INSERT INTO groups (group_name) VALUES (:#{#data.groupName})")
    Mono<GroupData> insertGroup(@Param("data") GroupData groupData);

}
