package cl.camanchaca.business.usecases.largoplazo.biomass;

import cl.camanchaca.business.generic.BusinessError;
import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.SizeRepository;
import cl.camanchaca.business.utils.SizeUtils;
import cl.camanchaca.domain.models.Size;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GetSizeBiomassBySpecieUseCase extends Usecase<String, Flux<Size>> {

    private final SizeRepository sizeRepository;

    public Mono<ParametersResponse> get() {
        return sizeRepository.count()
                .zipWith(sizeRepository.getAll()
                        .collectList()
                        )
                .map(tuple -> ParametersResponse.of(tuple.getT2(), tuple.getT1()))
                .onErrorResume(throwable -> {
                    throwable.printStackTrace();
                    return Mono.error(new BusinessError(throwable.getMessage()));
                });
    }

    @Override
    public Flux<Size> apply(String specie) {
        return sizeRepository.getBySpecie(specie)
                .sort((size1, size2) -> SizeUtils.sortSizes().compare(size1, size2));
    }
}
