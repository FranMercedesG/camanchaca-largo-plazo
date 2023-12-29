package cl.camanchaca.biomass.adapter.postgres.product.capaity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Table("product_capacity")
public class ProductCapacityData {

    @Id
    @Column("product_capacity_id")
    private UUID productCapacityId;

    @Column("products_id")
    private Integer productId;

    @Column("productive_capacity_id")
    private UUID productiveCapacityId;

    private Boolean status;

}
