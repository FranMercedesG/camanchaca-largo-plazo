package cl.camanchaca.capacity.adapters.postgres.period;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.util.UUID;
@Table("period_planification")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class PeriodData {
    @Id
    @Column("period_planification_id")
    private UUID id;

    @Column("initial_period")
    private LocalDate initialPeriod;

    @Column("final_period")
    private LocalDate finalPeriod;

    @Column("user_id")
    private String user;
}
