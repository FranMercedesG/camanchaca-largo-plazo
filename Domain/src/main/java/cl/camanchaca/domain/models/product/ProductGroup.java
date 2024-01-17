package cl.camanchaca.domain.models.product;

import cl.camanchaca.domain.models.parameters.ParameterGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductGroup {

    private Integer productId;
    private String species;
    private String description;
    private List<ParameterGroup> groups;

}
