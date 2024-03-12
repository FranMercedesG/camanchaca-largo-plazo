package cl.camanchaca.optimization.adapter.postgres.demandperiod;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DemandPeriodDto {
    private UUID demandPeriodId;
    private LocalDate period;
    private UUID demandId;
    private Boolean status;
    private String codigo;
    private String oficina;
    private String familia;
    private String pais;
    private String dv;
}
