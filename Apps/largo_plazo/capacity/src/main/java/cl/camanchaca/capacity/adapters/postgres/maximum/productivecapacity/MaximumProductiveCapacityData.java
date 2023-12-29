package cl.camanchaca.capacity.adapters.postgres.maximum.productivecapacity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "productive_capacity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaximumProductiveCapacityData {
    @Id
    @Column("productive_capacity_id")
    private UUID id;

    @Column("name")
    private String name;

}
