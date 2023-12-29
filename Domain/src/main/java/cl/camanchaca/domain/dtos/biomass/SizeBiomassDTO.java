package cl.camanchaca.domain.dtos.biomass;

import cl.camanchaca.domain.models.Size;
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
public class SizeBiomassDTO {

    private List<Size> data;
    private List<UUID> deletes;

}
