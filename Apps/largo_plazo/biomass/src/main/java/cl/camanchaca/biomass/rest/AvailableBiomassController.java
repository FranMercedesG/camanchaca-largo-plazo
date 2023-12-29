package cl.camanchaca.biomass.rest;

import cl.camanchaca.biomass.validations.BiomassValidations;
import cl.camanchaca.biomass.validations.ValidationAvailableBiomass;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.usecases.largoplazo.biomass.GetDailyAvailableBiomassUseCase;
import cl.camanchaca.business.usecases.largoplazo.biomass.GetWeeklyAvailableBiomassUseCase;
import cl.camanchaca.business.usecases.largoplazo.biomass.SaveAvailableBiomassUseCase;
import cl.camanchaca.business.usecases.largoplazo.biomass.bigquery.GetWeeklyAvailableBiomassBQUseCase;
import cl.camanchaca.domain.dtos.biomass.AvailableBiomassDTO;
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
public class AvailableBiomassController {

    private final MainErrorhandler errorhandler;

    private final String URL_BASE = "/biomass/available";

    @Bean
    public RouterFunction<ServerResponse> getADailyAvailableBiomass(GetDailyAvailableBiomassUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE+"/daily"),
                request ->  BiomassValidations
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
    public RouterFunction<ServerResponse> getWeeklyAvailableBiomass(GetWeeklyAvailableBiomassUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE+"/weekly"),
                request ->  BiomassValidations
                        .validateParamsPagination(request, RequestParams.builder().build())
                        .flatMapMany(useCase::apply)
                        .collectList()
                        .flatMap(r -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(r)
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(errorhandler::badRequest)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getWeeklyAvailableBiomassBQ(GetWeeklyAvailableBiomassBQUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE + "/weekly/bq"),
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
    public RouterFunction<ServerResponse> saveAvailableBiomass(SaveAvailableBiomassUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .POST(URL_BASE)
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request ->
                        ValidationAvailableBiomass
                                .validateAvailableBiomass(
                                        request.bodyToMono(AvailableBiomassDTO.class)
                                )
                                .flatMapMany(availableBiomasses -> useCase.apply(availableBiomasses))
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
