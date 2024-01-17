package cl.camanchaca.biomass.mappers.postgres;

import cl.camanchaca.biomass.adapter.postgres.group.GroupData;
import cl.camanchaca.domain.models.Group;

public class GroupMapper {
    public static Group toGroup(GroupData data){
        return Group.builder()
                .id(data.getGroupId())
                .name(data.getGroupName())
                .build();
    }

    public static GroupData toGroupData(Group data){
        return GroupData.builder()
                .groupId(data.getId())
                .groupName(data.getName())
                .build();
    }

    private GroupMapper(){}
}
