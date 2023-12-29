package cl.camanchaca.domain.models.parameters;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ParameterCapacity {

    private UUID id;
    private UUID columnId;
    private Boolean status;

}
