package cl.camanchaca.capacity.adapters.entrypoints.rest.capacity.baseperiodscenario;

import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.usecases.largoplazo.capacity.productivedays.GetAllProductiveDaysUseCase;
import cl.camanchaca.business.usecases.largoplazo.capacity.productivedays.SaveProductiveDaysUseCase;
import cl.camanchaca.capacity.validations.CapacityValidations;
import cl.camanchaca.domain.models.capacity.BasePeriodScenario;
import cl.camanchaca.generics.MainErrorhandler;
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
public class BasePeriodScenarioController {

    private final MainErrorhandler errorhandler;

    private final String URL_BASE = "/capacity/productive-days";
    @Bean
    public RouterFunction<ServerResponse> getAllProductiveDays(GetAllProductiveDaysUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE),
                request -> CapacityValidations
                        .validateParamsPagination(request, RequestParams.builder().build())
                        .flatMap(o -> {
                            CapacityValidations.validateHeader(request.headers());
                            String user = request.headers().header("user").get(0);
                            String office = request.headers().header("office").get(0);
                            Map<String, String> headerInfo = Map.of("user", user, "office", office);
                            return useCase.apply(o, headerInfo);
                        })
                        .flatMap(s ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    @Bean
    public RouterFunction<ServerResponse> saveBasePeriod(SaveProductiveDaysUseCase useCase) {
        return route(
                RequestPredicates
                        .PUT(URL_BASE)
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> CapacityValidations.validateRequestBody(request.bodyToFlux(BasePeriodScenario.class))
                        .collectList()
                        .flatMapMany(o -> {
                            CapacityValidations.validateHeader(request.headers());
                            String user = request.headers().header("user").get(0);
                            String office = request.headers().header("office").get(0);
                            Map<String, String> headerInfo = Map.of("user", user, "office", office);
                            return useCase.apply(o, headerInfo);
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
