package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.biomass.RealBiomassRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GetRealBiomassUseCase extends Usecase<RequestParams, Mono<ParametersResponse>> {

    private final RealBiomassRepository realBiomassRepository;

    @Override
    public Mono<ParametersResponse> apply(RequestParams params) {
        Integer offset = (params.getPage() - 1) * params.getSize();
        return realBiomassRepository.
                getAllByPage(params.getSize(), offset)
                .collectList()
                .zipWith(realBiomassRepository.count())
        .map(tuple -> ParametersResponse.of(tuple.getT1(), tuple.getT2()));
    }
}
