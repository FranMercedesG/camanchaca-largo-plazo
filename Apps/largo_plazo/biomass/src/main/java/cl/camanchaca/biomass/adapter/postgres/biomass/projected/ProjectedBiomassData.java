package cl.camanchaca.biomass.adapter.postgres.biomass.projected;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@Table("projected_biomass")
public class ProjectedBiomassData {

    @Id
    @Column("projected_biomass_id")
    private UUID projectedBiomassId;

    @Column("size_id")
    private UUID sizeId;

    @Column("projection_date")
    private LocalDate projectionDate;

    private long pieces;
    private BigDecimal quality;

    @Column("average_weight")
    private BigDecimal avgWeight;

    @Column("kilos_wfe")
    private BigDecimal kilosWFE;

    @Column("kiloswfe_scenario")
    private BigDecimal kilosWFEScenario;

}
