package cl.camanchaca.domain.models.demand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Freight {
    private String port;
    private String sectorNum;
    private String formaGrupo;
    private String incoterms;
    private Float freightValue;
}
