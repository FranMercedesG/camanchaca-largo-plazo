package cl.camanchaca.domain.models.biomass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class AvailableBiomass {

    private String hu;
    private String externalHu;
    private String type;
    private String size;
    private String quality;
    private String header;
    private String manufacturingOrder;
    private String batch;
    private String folio;
    private String center;
    private String fecpro;
    private Integer permanency;
    private Double pieces;
    private Double weight;
    private String specie;
}
