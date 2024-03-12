package cl.camanchaca.domain.models.optimization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class DemandPeriod {
    private UUID unrestrictedDemandId;
    private String codigo;
    private String oficina;
    private String familia;
    private String pais;
    private String dv;
    private List<Period> periods;
}
