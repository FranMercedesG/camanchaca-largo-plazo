package cl.camanchaca.parametrization.mappers;

import cl.camanchaca.domain.models.Group;
import cl.camanchaca.parametrization.adapter.postgresql.group.GroupData;

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
}
