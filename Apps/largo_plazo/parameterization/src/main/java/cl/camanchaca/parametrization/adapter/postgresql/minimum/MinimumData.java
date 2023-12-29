package cl.camanchaca.parametrization.adapter.postgresql.minimum;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Table("minimum")
public class MinimumData {

    @Id
    @Column("minimum_id")
    private UUID minimumId;
    private String name;

}
