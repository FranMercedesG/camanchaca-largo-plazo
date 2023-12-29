package cl.camanchaca.domain.dtos;

import cl.camanchaca.domain.models.demand.PrecioCierreUsdKgNetoDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceRequestDto {
    private String incoterm;
    private String unit;
    private List<PrecioCierreUsdKgNetoDetail> purchPrice;
}
