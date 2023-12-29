package cl.camanchaca.parametrization.adapter.postgresql.product.size;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Table("product_size")
public class ProductSizeData {

    @Id
    @Column("size_product_id")
    private UUID sizeProductId;

    @Column("products_id")
    private Integer productId;

    @Column("size_id")
    private UUID sizeId;

    private BigDecimal performance;

    private Boolean status;

}
