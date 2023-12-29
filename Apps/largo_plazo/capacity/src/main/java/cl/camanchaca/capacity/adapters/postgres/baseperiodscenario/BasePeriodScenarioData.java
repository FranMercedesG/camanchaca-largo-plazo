package cl.camanchaca.capacity.adapters.postgres.baseperiodscenario;
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

@Table(name = "base_period_scenario")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BasePeriodScenarioData {
    @Id
    @Column("base_period_scenario_id")
    private UUID id;

    @Column("pieces")
    private String pieces;

    @Column("kilos_wfe")
    private String kilosWfe;

    @Column("average_weight")
    private BigDecimal averageWeight;

    @Column("work_days")
    private Integer workDays;

    @Column("extra_days")
    private Integer extraDays;

    @Column("period")
    private LocalDate period;

}
