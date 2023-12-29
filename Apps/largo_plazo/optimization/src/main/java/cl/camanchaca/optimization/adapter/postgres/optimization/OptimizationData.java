package cl.camanchaca.optimization.adapter.postgres.optimization;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Table
public class OptimizationData {

    @Column("unrestricted_demand_id")
    private UUID unrestricterDemandId;
    @Column("size_id")
    private UUID sizeId;
    private LocalDate period;
    @Column("kilo_wfe")
    private BigDecimal kiloWfe;
    @Column("products_id")
    private Integer codigo;
    @Column("order_monthly_planification_id")
    private UUID orderMonthlyPlanificationId;

}
