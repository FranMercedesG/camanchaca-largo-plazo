package cl.camanchaca.business.usecases.shared;

import cl.camanchaca.business.generic.Usecase;
import reactor.core.publisher.Mono;

public class CamanchacaUseCase  extends Usecase<Object, Mono<String>> {



    @Override
    public Mono<String> apply(Object any) {
        return Mono.just("TODO implement usecase");
    }
}
