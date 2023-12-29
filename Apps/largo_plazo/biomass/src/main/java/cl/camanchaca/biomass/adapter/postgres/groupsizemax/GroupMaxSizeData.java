package cl.camanchaca.biomass.adapter.postgres.groupsizemax;

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
@Table("max_size_group")
public class GroupMaxSizeData {

    @Id
    @Column("max_size_group_id")
    private UUID maxSizeGroupId;

    @Column("group_id")
    private UUID groupId;

    @Column("size_id")
    private UUID sizeId;

    @Column("period")
    private LocalDate period;

    @Column("maximum_limit")
    private BigDecimal maximumLimit;




}
