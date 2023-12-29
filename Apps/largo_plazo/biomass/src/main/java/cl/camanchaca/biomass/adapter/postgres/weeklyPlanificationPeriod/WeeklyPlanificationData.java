package cl.camanchaca.biomass.adapter.postgres.weeklyPlanificationPeriod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Table("weekly_planification_period")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyPlanificationData {

    @Id
    @Column("id_weekly_planification_period")
    private Long id;

    @Column("week_planification")
    private LocalDate week;

}
