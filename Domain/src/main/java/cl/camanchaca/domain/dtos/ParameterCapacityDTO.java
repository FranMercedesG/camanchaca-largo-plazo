package cl.camanchaca.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ParameterCapacityDTO {
    private UUID id;
    private Integer productId;
    private UUID productiveCapacityId;
    private UUID plantId;
    private Boolean status;
}
