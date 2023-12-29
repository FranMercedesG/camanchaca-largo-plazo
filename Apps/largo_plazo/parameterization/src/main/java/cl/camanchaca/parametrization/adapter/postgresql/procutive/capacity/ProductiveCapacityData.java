package cl.camanchaca.parametrization.adapter.postgresql.procutive.capacity;


import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Table("productive_capacity")
public class ProductiveCapacityData {

    @Id
    @Column("productive_capacity_id")
    private UUID productiveCapacityId;
    private String name;
    private String description;

}
