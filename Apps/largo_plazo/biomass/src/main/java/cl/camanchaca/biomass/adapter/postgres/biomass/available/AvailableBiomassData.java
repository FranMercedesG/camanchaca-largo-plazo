package cl.camanchaca.biomass.adapter.postgres.biomass.available;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder(toBuilder = true)
@Table("avalaible_biomass")
public class AvailableBiomassData {

    @Id
    private String hu;

    @Column("external_hu")
    private String externalHu;

    private String type;

    private String size;

    private String quality;

    private String header;

    @Column("manufacturing_order")
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
