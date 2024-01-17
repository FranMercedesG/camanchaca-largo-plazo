package cl.camanchaca.domain.models.product;

import cl.camanchaca.domain.models.parameters.ParameterCapacity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductCapacity {

    private Integer productId;
    private String species;
    private String description;
    private List<ParameterCapacity> capacities;

}
