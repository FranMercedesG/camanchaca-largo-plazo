package cl.camanchaca.biomass.adapter.postgres.group;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Table("groups")
public class GroupData {

    @Id
    @Column("group_id")
    private UUID groupId;

    @Column("group_name")
    private String groupName;

}
