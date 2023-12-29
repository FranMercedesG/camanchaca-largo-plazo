package cl.camanchaca.business.usecases.largoplazo.parameters;

import cl.camanchaca.business.generic.ParametersResponse;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.generic.Usecase;
import cl.camanchaca.business.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class GetParametersSKUUseCase extends Usecase<RequestParams, Mono<ParametersResponse>> {

    private final ProductRepository productRepository;

    @Override
    public Mono<ParametersResponse> apply(RequestParams pageAndSize) {
        return productRepository.getByPageAndSize(pageAndSize.getPage(), pageAndSize.getSize())
                .collectList()
                .zipWith(productRepository.count())
                .map(tuple -> ParametersResponse.of(
                        tuple.getT1(),
                        tuple.getT2()
                ));
    }
}
