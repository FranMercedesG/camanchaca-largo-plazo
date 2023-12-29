package cl.camanchaca.domain.dtos;

import cl.camanchaca.domain.models.demand.KilosNetosDetail;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenerateKgWFEDemandResponseDto {
    private BigDecimal rend;
    private List<KilosNetosDetail> kgWFEDemand;
}
