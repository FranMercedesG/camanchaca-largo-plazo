package cl.camanchaca.capacity.adapters.postgres.minimum.daily;

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

@Table(name = "minimum_daily_period")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MinimumPeriodDailyProductiveCapacityData {
    @Id
    @Column("minimum_daily_period_id")
    private UUID id;

    @Column("minimum_value")
    private BigDecimal maximum;

    @Column("period")
    private LocalDate period;

    @Column("minimum_id")
    private UUID productiveCapacityId;

}
