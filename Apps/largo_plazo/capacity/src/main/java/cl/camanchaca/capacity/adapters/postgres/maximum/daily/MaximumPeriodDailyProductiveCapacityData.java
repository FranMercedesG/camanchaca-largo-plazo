package cl.camanchaca.capacity.adapters.postgres.maximum.daily;
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

@Table(name = "period_daily_productive_capacity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaximumPeriodDailyProductiveCapacityData {
    @Id
    @Column("period_daily_productive_capacity_id")
    private UUID id;

    @Column("maximum_capacity")
    private BigDecimal maximum;

    @Column("period")
    private LocalDate period;

    @Column("productive_capacity_id")
    private UUID productiveCapacityId;
}
