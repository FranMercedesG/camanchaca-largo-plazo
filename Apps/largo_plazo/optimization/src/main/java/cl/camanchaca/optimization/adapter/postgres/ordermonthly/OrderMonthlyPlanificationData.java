package cl.camanchaca.optimization.adapter.postgres.ordermonthly;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table("order_monthly_planification")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderMonthlyPlanificationData {

    @Id
    @Column("order_monthly_planification_id")
    private UUID orderMonthlyPlanificationId;

    @Column("monthly_planification_id")
    private UUID monthlyPlanificationId;

    @Column("unrestricted_demand_id")
    private UUID unrestrictedDemandId;

    @Column("size_id")
    private UUID sizeId;

    @Column("kilo_wfe")
    private BigDecimal kiloWfe;

    @Column("period")
    private LocalDate period;

}
