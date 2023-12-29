package cl.camanchaca.optimization.adapter.postgres.monthly;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;

@Table("monthly_planification")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyPlanificationData {

    @Id
    @Column("monthly_planification_id")
    private UUID monthlyPlanificationId;

    @Column("initial_period")
    private LocalDate initialPeriod;

    @Column("final_period")
    private LocalDate finalPeriod;

    @Column("creation_date")
    private LocalDate creationDate;

    @Column("version")
    private Integer version;

    @Column("active")
    private Boolean active;


}
