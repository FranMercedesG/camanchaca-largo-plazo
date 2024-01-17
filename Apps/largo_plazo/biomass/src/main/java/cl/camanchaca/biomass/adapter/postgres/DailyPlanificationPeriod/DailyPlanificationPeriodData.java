package cl.camanchaca.biomass.adapter.postgres.dailyplanificationperiod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("daily_planification_period")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DailyPlanificationPeriodData {

    @Id
    @Column("id_daily_planification_period")
    private Long id;

    @Column("day_planification")
    private LocalDate day;

}
