package cl.camanchaca.parametrization.adapter.postgresql.product.group;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Table("product_group")
public class ProductGroupData {

    @Id
    @Column("product_group_id")
    private UUID id;

    @Column("products_id")
    private Integer productId;

    @Column("group_id")
    private UUID groupId;

    @Column("status")
    private Boolean status;



}
