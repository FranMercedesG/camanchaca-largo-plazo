package cl.camanchaca.domain.models.capacity.minimum;

import lombok.*;

import java.util.List;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MinimumCapacity {
    private String name;
    private List<MinimumCapacityValue> capacity;
}
