package cl.camanchaca.business.responses;

import cl.camanchaca.domain.models.biomass.GroupSizeMax;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GroupSizeMaxResponse {
    private List<GroupSizeMax> data;
    private List<UUID> deletes;
}
