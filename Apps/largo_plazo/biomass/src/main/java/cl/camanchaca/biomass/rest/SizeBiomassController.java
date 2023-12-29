package cl.camanchaca.biomass.rest;


import cl.camanchaca.biomass.validations.ValidationSizeBiomass;
import cl.camanchaca.business.usecases.largoplazo.biomass.GetSizeBiomassBySpecieUseCase;
import cl.camanchaca.business.usecases.largoplazo.biomass.SaveSizeBiomassUseCase;
import cl.camanchaca.domain.dtos.biomass.SizeBiomassDTO;
import cl.camanchaca.generics.MainErrorhandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
@AllArgsConstructor
public class SizeBiomassController {

    private final MainErrorhandler errorhandler;

    private final String URL_BASE = "/biomass/size";

    @Bean
    public RouterFunction<ServerResponse> getSizes(GetSizeBiomassBySpecieUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.GET(URL_BASE + "/{specie}"),
                request -> Mono.fromCallable(() ->
                                request.pathVariable("specie"))
                        .map(String::toUpperCase)
                        .flatMapMany(specie -> useCase.apply(specie))
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

    @Bean
    public RouterFunction<ServerResponse> saveSizes(SaveSizeBiomassUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.POST(URL_BASE),
                request -> ValidationSizeBiomass
                        .validateSizeBiomass(request.bodyToMono(SizeBiomassDTO.class))
                        .flatMapMany(useCase::apply)
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
