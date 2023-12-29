package cl.camanchaca.optimization.validations;

import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class OptimizationValidations {
    public static void validateHeader(ServerRequest.Headers headers) {
        Stream.of(
                headers.header("user").get(0),
                headers.header("office").get(0)
        ).forEach(Objects::requireNonNull);
    }


}
