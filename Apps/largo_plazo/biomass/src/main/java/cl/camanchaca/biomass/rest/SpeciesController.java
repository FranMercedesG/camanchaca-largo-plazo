package cl.camanchaca.biomass.rest;

import cl.camanchaca.business.usecases.largoplazo.biomass.GetSpeciesUseCase;
import cl.camanchaca.generics.MainErrorhandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@AllArgsConstructor
public class SpeciesController {

    private final MainErrorhandler errorhandler;

    private final String URL_BASE = "/biomass/species";

    @Bean
    public RouterFunction<ServerResponse> getSpecies(GetSpeciesUseCase useCase){
        return RouterFunctions.route(
                RequestPredicates.GET(URL_BASE),
                request -> useCase.get()
                        .collectList()
                        .flatMap(result ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result)
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(errorhandler::badRequest)
        );
    }

}
