package cl.camanchaca.orders.entrypoints.rest.demand;

import cl.camanchaca.business.usecases.largoplazo.orders.demand.SaveUnrestrictedDemandAdminUseCase;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.orders.validations.DemandValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
@Configuration
@RequiredArgsConstructor
public class UnrestrictedDemandController {

    private final MainErrorhandler errorhandler;

    private final String URL_BASE = "/demand";
    @Bean
    public RouterFunction<ServerResponse> saveDemand(SaveUnrestrictedDemandAdminUseCase useCase) {
        return route(
                RequestPredicates
                        .PUT(URL_BASE)
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> DemandValidations.validateRequestBody(request.bodyToFlux(UnrestrictedDemand.class))
                        .collectList()
                        .flatMapMany(o -> {
                            DemandValidations.validateHeader(request.headers());
                            String user = request.headers().header("user").get(0);
                            String office = request.headers().header("office").get(0);
                            Map<String, String> headerInfo = Map.of("user", user, "office", office);
                            return useCase.apply(o, headerInfo, "plataforma");
                        })
                        .collectList()
                        .flatMap(s ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
        );
    }
}
