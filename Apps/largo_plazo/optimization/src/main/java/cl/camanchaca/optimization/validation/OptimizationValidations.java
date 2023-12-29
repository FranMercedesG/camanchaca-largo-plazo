package cl.camanchaca.optimization.validation;

import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.generics.errors.InfrastructureError;
import cl.camanchaca.utils.LocalDateUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class OptimizationValidations {
    public static Mono<LocalDate> validateMonth(ServerRequest request) {
        return Mono.fromCallable(() -> "2023-10-12")
//        Mono.fromCallable(() -> request.queryParam("month")
//                        .get())
                .map(LocalDateUtils::parseStringToDate)
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }
    public static Mono<Integer> validateVersion(ServerRequest request) {
        return Mono.fromCallable(() -> request.queryParam("version")
                        .get())
                .map(Integer::parseInt)
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }

    public static Mono<RequestParams> validateParamsPagination(ServerRequest request, RequestParams params) {
        return Mono.fromCallable(() -> request.queryParam("page")
                        .get())
                .map(page -> {
                    int pageInt = Integer.parseInt(page);
                    int pageSize = Integer.parseInt(request.queryParam("pageSize").get());
                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new RuntimeException("La pagina o el tamaño no puede ser menor a 0");
                    }
                    return params.toBuilder()
                            .page(pageInt)
                            .size(pageSize)
                            .build();
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }

    public static void validateHeader(ServerRequest.Headers headers) {
        Stream.of(
                headers.header("user").get(0),
                headers.header("office").get(0)
        ).forEach(Objects::requireNonNull);

    }

      private OptimizationValidations(){}
}
