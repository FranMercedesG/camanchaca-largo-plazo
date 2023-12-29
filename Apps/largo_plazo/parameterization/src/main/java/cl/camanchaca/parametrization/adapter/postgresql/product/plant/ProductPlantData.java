package cl.camanchaca.parametrization.adapter.postgresql.product.plant;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Table("product_plant")
public class ProductPlantData {

    @Id
    @Column("product_plant_id")
    private UUID productPlantId;

    @Column("products_id")
    private Integer productId;

    @Column("plant_id")
    private UUID plantId;

    private Boolean status;

}
