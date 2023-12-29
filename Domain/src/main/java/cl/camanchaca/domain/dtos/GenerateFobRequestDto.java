package cl.camanchaca.domain.dtos;

import cl.camanchaca.domain.models.demand.FobDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GenerateFobRequestDto {
    private String incoterm;
    private BigDecimal freight;
    private BigDecimal insurance;
    private List<FobDetail> purchPrice;
}
