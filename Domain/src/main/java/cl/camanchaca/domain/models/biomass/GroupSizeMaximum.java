package cl.camanchaca.domain.models.biomass;

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
public class GroupSizeMaximum {
    private UUID groupId;
    private UUID sizeId;
    private String specieName;
    private String groupName;
    private String sizeName;
    private String qualityName;
    private List<GroupSizeMaxValue> value;
}
