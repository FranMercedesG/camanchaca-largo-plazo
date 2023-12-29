package cl.camanchaca.domain.models.product;

import cl.camanchaca.domain.models.parameters.ParameterPerformance;
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
public class ProductPerformance {

    private Integer productId;
    private String species;
    private String description;
    private List<ParameterPerformance> performances;

}
