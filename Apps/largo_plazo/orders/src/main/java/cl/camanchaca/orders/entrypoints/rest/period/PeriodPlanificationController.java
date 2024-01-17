package cl.camanchaca.orders.entrypoints.rest.period;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.usecases.largoplazo.orders.SavePeriodsUseCase;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.orders.validations.ValidationsTurn;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PeriodPlanificationController {
    public static final String BASE_URL = "/home/period";
    private final MainErrorhandler errorhandler;

    @Bean
    public RouterFunction<ServerResponse> savePeriod(SavePeriodsUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.GET(BASE_URL),
                request ->
                        ValidationsTurn.validateParams(request)
                                .flatMap( o -> {
                                    ValidationsTurn.validateHeader(request.headers());
                                    String user = request.headers().header(Constans.USER.getValue()).get(0);
                                    String office = request.headers().header(Constans.OFFICE.getValue()).get(0);
                                    Map<String, String> headerInfo = Map.of(Constans.USER.getValue(), user, Constans.OFFICE.getValue(), office);
                                    return useCase.apply(o, headerInfo);
                                })
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
