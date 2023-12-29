package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.domain.dtos.GenerateRmpRequestDto;
import cl.camanchaca.domain.dtos.GenerateRmpResponseDto;
import cl.camanchaca.domain.models.demand.RmpDetail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class GetAutoCalculateRmpUseCase {

    public Mono<GenerateRmpResponseDto> apply(GenerateRmpRequestDto body, Map<String, String> header) {
        GenerateRmpResponseDto fob = generateRMP(body);
        return Mono.just(fob);
    }

    private GenerateRmpResponseDto generateRMP(GenerateRmpRequestDto body) {
        GenerateRmpResponseDto response = generateResponse(body);

        List<RmpDetail> rmpDetailList = body.getFob().stream()
                .map(r -> {
                    BigDecimal substractCost = r.getValue()
                            .subtract(body.getCostDist())
                            .subtract(body.getCostEmpt())
                            .subtract(body.getCostProc());

                    BigDecimal rmp = substractCost.multiply(body.getRend())
                            .add(body.getAporteNeto());

                    return RmpDetail.builder().value(rmp).date(r.getDate()).build();
                })
                .collect(Collectors.toList());

        response.setRmp(rmpDetailList);
        return response;
    }

    private GenerateRmpResponseDto generateResponse(GenerateRmpRequestDto r) {
        return GenerateRmpResponseDto
                .builder()
                .costEmpt(r.getCostEmpt())
                .costDist(r.getCostDist())
                .costProc(r.getCostProc())
                .aporteNeto(r.getAporteNeto())
                .rend(r.getRend())
                .build();
    }
}
