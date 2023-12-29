package cl.camanchaca.domain.dtos;

import cl.camanchaca.domain.models.demand.KilosNetosDetail;
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
public class GenerateKgWFEDemandRequestDto {
    private BigDecimal rend;
    private List<KilosNetosDetail> kilosNetos;
}
