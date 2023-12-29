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
public class ParameterPlantDTO {

    private UUID id;
    private Integer productId;
    private UUID plantId;
    private Boolean status;

}
