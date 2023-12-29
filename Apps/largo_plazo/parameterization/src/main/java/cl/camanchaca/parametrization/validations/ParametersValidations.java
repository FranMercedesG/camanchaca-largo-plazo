package cl.camanchaca.parametrization.validations;

import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.generics.errors.InfrastructureError;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class ParametersValidations {
    private ParametersValidations() {

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

}
