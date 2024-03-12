package cl.camanchaca.optimization.adapter.postgres.demandperiod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table(name = "demand_period")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandPeriodData {
    @Id
    @Column("demand_period_id")
    private UUID demandPeriodId;

    @Column("period")
    private LocalDate period;

    @Column("demand_id")
    private UUID unrestrictedDemandId;

    @Column("status")
    private Boolean status;

}
