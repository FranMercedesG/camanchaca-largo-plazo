package cl.camanchaca.parametrization.adapter.postgresql.size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table("size")
public class SizeData {

    @Id
    @Column("size_id")
    private UUID sizeId;
    private String unit;
    @Column("minimum_range")
    private BigDecimal minRange;
    @Column("maximun_range")
    private BigDecimal maxRange;
}
