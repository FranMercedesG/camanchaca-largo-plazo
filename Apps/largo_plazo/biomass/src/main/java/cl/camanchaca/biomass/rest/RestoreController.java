package cl.camanchaca.biomass.rest;

import cl.camanchaca.business.usecases.largoplazo.biomass.RestoreBIomassUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RestoreController {

    private final String URL_BASE = "/restore/biomass";

    @Bean
    public RouterFunction<ServerResponse> restoreBiomass(RestoreBIomassUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE),
                request -> {
                    try {
                        return useCase.restore()
                                .flatMap(string -> ServerResponse.ok()
                                        .bodyValue(string));
                    } catch (InterruptedException e) {
                        return ServerResponse.badRequest()
                                .build();
                    }
                }
        );
    }

}
