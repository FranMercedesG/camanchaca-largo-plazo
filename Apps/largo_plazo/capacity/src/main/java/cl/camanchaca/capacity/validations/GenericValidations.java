package cl.camanchaca.capacity.validations;

import cl.camanchaca.generics.errors.InfrastructureError;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

public class GenericValidations {

    public static Mono<String> validateHeader(ServerRequest serverRequest, String header) {
        if (serverRequest.headers().header(header)
                .isEmpty()) {
            return Mono.error(new InfrastructureError("01"));
        }
        return Mono.just(serverRequest.headers().header(header).get(0));
    }

    public static Mono<String> validateQueryParam(ServerRequest serverRequest, String param) {
        if (serverRequest.queryParam(param).isEmpty()) {
            return Mono.error(new InfrastructureError("02"));
        }
        return Mono.just(serverRequest.queryParam(param).get());
    }


}
