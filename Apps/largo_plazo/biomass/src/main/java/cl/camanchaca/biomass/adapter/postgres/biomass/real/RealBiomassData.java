package cl.camanchaca.biomass.adapter.postgres.biomass.real;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder(toBuilder = true)
@Table("real_biomass")
public class RealBiomassData {
    @Id
    private String hu;
    private String plant;
    private String quality;
    private String size;
    private Double pieces;
    private Double weight;
    private String specie;

}
