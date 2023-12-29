package cl.camanchaca.capacity.adapters.postgres.minimum.minimumcapacity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("minimum")
public class MinimumData {
    @Id
    @Column("minimum_id")
    private UUID minimumId;
    @Column("name")
    private String name;
}
