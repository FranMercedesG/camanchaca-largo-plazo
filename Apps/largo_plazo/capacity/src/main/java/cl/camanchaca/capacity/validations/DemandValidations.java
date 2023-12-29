package cl.camanchaca.capacity.validations;

import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import cl.camanchaca.generics.errors.InfrastructureError;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class DemandValidations {

    public DemandValidations() {
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

    public static Flux<UnrestrictedDemand> validateRequestBody(Flux<UnrestrictedDemand> body) {
        return body
                .<UnrestrictedDemand>handle((dto, sink) -> {
                    try {
                        Stream.of(
                                dto.getUsuario(),
                                dto.getPeriodo(),
                                dto.getOfficeSalesType(),
                                dto.getOficina()
                        ).forEach(Objects::requireNonNull);
                        sink.next(dto);
                    } catch (Exception e) {
                        // Agregar información de registro aquí
                        System.out.println("Error de validación en validateRequestBody" + e);
                        sink.error(e);
                    }
                })
                .onErrorResume(throwable -> Mono.error(new RuntimeException(throwable.getLocalizedMessage())));
    }

    public static void validateHeader(ServerRequest.Headers headers) {
        Stream.of(
                headers.header("user").get(0),
                headers.header("office").get(0)
        ).forEach(Objects::requireNonNull);

    }
}
