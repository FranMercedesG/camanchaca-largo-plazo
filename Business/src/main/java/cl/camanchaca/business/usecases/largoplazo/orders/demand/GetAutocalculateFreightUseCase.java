package cl.camanchaca.business.usecases.largoplazo.orders.demand;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.repositories.bigquery.DemandUnrestrictedBQRepository;
import cl.camanchaca.domain.dtos.FreightRequestDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@AllArgsConstructor
public class GetAutocalculateFreightUseCase {

    private DemandUnrestrictedBQRepository bigQueryDemand;

    public Mono<ParametersResponse> apply(FreightRequestDto body, Map<String, String> header) throws InterruptedException {

        return bigQueryDemand.getFreightsByPortAndIncoterms(body.getPort(),
                        body.getIncoterms())
                .collectList()
                .flatMap(o -> {
                    return Mono.just(ParametersResponse.of(o, (long) o.size()));
                });

    }

    public Mono<Float> getValue(FreightRequestDto body, Map<String, String> header) throws InterruptedException {

        return bigQueryDemand.getFreightsByPortAndIncoterms(body.getPort(),
                        body.getIncoterms())
                .collectList()
                .map(o -> {
                    return o.get(0).getFreightValue();
                });

    }
}
