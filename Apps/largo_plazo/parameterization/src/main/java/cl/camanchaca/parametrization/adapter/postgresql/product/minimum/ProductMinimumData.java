package cl.camanchaca.parametrization.adapter.postgresql.product.minimum;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Table("product_minimum")
public class ProductMinimumData {

    @Id
    @Column("product_minimun_id")
    private UUID id;

    @Column("products_id")
    private Integer productId;

    @Column("minimum_id")
    private UUID minimumId;

    @Column("status")
    private Boolean status;
}
