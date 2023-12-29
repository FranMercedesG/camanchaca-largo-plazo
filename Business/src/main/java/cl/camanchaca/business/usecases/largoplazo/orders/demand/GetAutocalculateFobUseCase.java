package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.domain.dtos.GenerateFobRequestDto;
import cl.camanchaca.domain.dtos.GenerateFobResponseDto;
import cl.camanchaca.domain.models.demand.FobDetail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class GetAutocalculateFobUseCase {
    public Mono<GenerateFobResponseDto> apply(GenerateFobRequestDto body, Map<String, String> header) {
        GenerateFobResponseDto fob = generateFob(body);
        return Mono.just(fob);
    }

    private GenerateFobResponseDto generateFob(GenerateFobRequestDto body) {
        GenerateFobResponseDto response = generateResponse(body);
        if (body.getIncoterm().equalsIgnoreCase("CIF") || body.getIncoterm().equalsIgnoreCase("CFR")) {
            List<FobDetail> fobs = body.getPurchPrice().stream()
                    .map(f -> {
                        BigDecimal fobCalculate = f.getValue()
                                .subtract(body.getFreight())
                                .subtract(body.getInsurance());

                        return FobDetail.builder()
                                .value(fobCalculate)
                                .date(f.getDate())
                                .build();
                    })
                    .collect(Collectors.toList());
            response.setFob(fobs);
        }

        return response;
    }

    private GenerateFobResponseDto generateResponse(GenerateFobRequestDto body) {
        return GenerateFobResponseDto
                .builder()
                .incoterm(body.getIncoterm())
                .freight(body.getFreight())
                .insurance(body.getInsurance())
                .build();
    }


}
