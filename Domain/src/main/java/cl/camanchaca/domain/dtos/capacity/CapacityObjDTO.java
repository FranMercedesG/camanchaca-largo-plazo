package cl.camanchaca.domain.dtos.capacity;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CapacityObjDTO {
    private UUID uuid;
    private String name;
}
