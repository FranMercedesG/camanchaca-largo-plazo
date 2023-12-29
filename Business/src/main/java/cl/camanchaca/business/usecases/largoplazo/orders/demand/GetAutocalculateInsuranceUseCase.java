package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.domain.dtos.InsuranceRequestDto;
import cl.camanchaca.domain.dtos.InsuranceResponse;
import cl.camanchaca.domain.models.demand.PrecioCierreUsdKgNetoDetail;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class GetAutocalculateInsuranceUseCase {
    public Mono<InsuranceResponse> apply(InsuranceRequestDto body, Map<String, String> header) {

        BigDecimal insuranceAmount = calculateInsurance(body);

        return Mono.just(InsuranceResponse
                .builder()
                .insuranceAmount(insuranceAmount)
                .build());
    }

    public BigDecimal calculateInsurance(InsuranceRequestDto body) {
        if (body.getPurchPrice() == null || body.getPurchPrice().isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal avg = calculateAverage(body.getPurchPrice());

        if ("CIF".equalsIgnoreCase(body.getIncoterm())) {
            if ("LB".equalsIgnoreCase(body.getUnit())) {
                return avg.multiply(new BigDecimal("2"));
            } else {
                return avg.multiply(new BigDecimal("0.0111712"));
            }
        } else {
            return avg.multiply(new BigDecimal("0.0111712"));
        }
    }


    private BigDecimal calculateAverage(List<PrecioCierreUsdKgNetoDetail> purchPriceList) {

        if (purchPriceList == null || purchPriceList.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = purchPriceList.stream()
                .map(PrecioCierreUsdKgNetoDetail::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return sum.divide(BigDecimal.valueOf(purchPriceList.size()), 2, BigDecimal.ROUND_HALF_UP);

    }
}
