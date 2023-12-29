package cl.camanchaca.generics;

import cl.camanchaca.domain.generic.ErrorGeneric;
import cl.camanchaca.generics.errors.ErrorResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.BiFunction;

public class MainErrorhandler {

    private static final BiFunction<HttpStatus, String, Mono<ServerResponse>> response =
            (status, value) -> ServerResponse.status(status).body(Mono.just(new ErrorResponse(value)),
                    ErrorResponse.class);


    public Mono<ServerResponse> badRequest(Throwable error) {
        error.printStackTrace();
        if (error instanceof ServerWebInputException) {
            var err = (ServerWebInputException) error;
            return response.apply(HttpStatus.BAD_REQUEST, Objects.requireNonNull(err.getRootCause()).getMessage());
        }
        if (error instanceof ErrorGeneric) {
            return ServerResponse.badRequest().body(Mono.just(new ErrorResponse(error.getMessage(),
                            ((ErrorGeneric) error)
                                    .getType()
                                    .orElse("Unknow")
                    )),
                    ErrorResponse.class);
        }
        return response.apply(HttpStatus.BAD_REQUEST, error.getMessage());
    }


}
