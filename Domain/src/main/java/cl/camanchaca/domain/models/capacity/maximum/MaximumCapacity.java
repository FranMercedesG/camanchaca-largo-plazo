package cl.camanchaca.domain.models.capacity.maximum;

import lombok.*;
import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaximumCapacity {
    private String name;
    private List<MaximumCapacityValue> capacity;
}
