package cl.camanchaca.biomass.rest;

import cl.camanchaca.biomass.validations.BiomassValidations;
import cl.camanchaca.biomass.validations.ValidationRealBiomass;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.usecases.largoplazo.biomass.GetRealBiomassUseCase;
import cl.camanchaca.business.usecases.largoplazo.biomass.SaveRealBiomassUseCase;
import cl.camanchaca.domain.models.biomass.RealBiomass;
import cl.camanchaca.generics.MainErrorhandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RealBiomassController {

    private final MainErrorhandler errorhandler;

    private static final String URL_BASE = "/biomass/real";

    @Bean
    public RouterFunction<ServerResponse> getRealBiomass(GetRealBiomassUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE),
                request -> BiomassValidations
                        .validateParamsPagination(request, RequestParams.builder().build())
                        .flatMap(useCase::apply)
                        .flatMap(r -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(r)
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(errorhandler::badRequest)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> saveRealBiomass(SaveRealBiomassUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .POST(URL_BASE)
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request ->
                        ValidationRealBiomass
                                .validateRealBiomass(
                                        request.bodyToFlux(RealBiomass.class)
                                )
                                .collectList()
                                .flatMapMany(useCase)
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
