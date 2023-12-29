package cl.camanchaca.domain.dtos;

import cl.camanchaca.domain.models.demand.FobDetail;
import cl.camanchaca.domain.models.demand.RmpDetail;
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
public class GenerateRmpResponseDto {
    private BigDecimal costEmpt;
    private BigDecimal costDist;
    private  BigDecimal costProc;
    private BigDecimal aporteNeto;
    private BigDecimal rend;
    private List<RmpDetail> rmp;
}
