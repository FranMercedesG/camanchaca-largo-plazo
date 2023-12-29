package cl.camanchaca.domain.dtos.group;

import cl.camanchaca.domain.dtos.ParameterGroupDTO;
import lombok.*;

import java.util.List;
import java.util.UUID;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GroupSKUParameterDTO {
    private List<ParameterGroupDTO> data;
    private List<UUID> deletes;
}
