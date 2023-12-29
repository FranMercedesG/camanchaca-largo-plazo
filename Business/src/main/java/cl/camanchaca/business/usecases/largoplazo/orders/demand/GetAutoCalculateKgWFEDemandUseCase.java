package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.domain.dtos.GenerateKgWFEDemandRequestDto;
import cl.camanchaca.domain.dtos.GenerateKgWFEDemandResponseDto;
import cl.camanchaca.domain.models.demand.KilosNetosDetail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class GetAutoCalculateKgWFEDemandUseCase {

    public Mono<GenerateKgWFEDemandResponseDto> apply(GenerateKgWFEDemandRequestDto body, Map<String, String> header) {
        GenerateKgWFEDemandResponseDto demandKgWFE = generateKGWFE(body);
        return Mono.just(demandKgWFE);
    }

    public BigDecimal calculateKGWFE(BigDecimal kilosNetos, BigDecimal rend) {
        return kilosNetos
                .divide(rend, 2, RoundingMode.HALF_UP);
    }

    private GenerateKgWFEDemandResponseDto generateKGWFE(GenerateKgWFEDemandRequestDto body) {
        GenerateKgWFEDemandResponseDto response = generateResponse(body);

        List<KilosNetosDetail> kgWFEDemand = body.getKilosNetos().stream()
                .map(kilosNetos -> {
                    BigDecimal valueKgWFE = kilosNetos.getValue()
                            .divide(body.getRend(), 2, RoundingMode.HALF_UP);
                    return KilosNetosDetail.builder().value(valueKgWFE).date(kilosNetos.getDate()).build();
                })
                .collect(Collectors.toList());

        response.setKgWFEDemand(kgWFEDemand);
        return response;
    }

    private GenerateKgWFEDemandResponseDto generateResponse(GenerateKgWFEDemandRequestDto r) {
        return GenerateKgWFEDemandResponseDto
                .builder()
                .rend(r.getRend())
                .kgWFEDemand(r.getKilosNetos())
                .build();
    }
}
