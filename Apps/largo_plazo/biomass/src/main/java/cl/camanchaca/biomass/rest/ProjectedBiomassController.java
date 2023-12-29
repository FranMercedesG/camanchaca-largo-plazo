package cl.camanchaca.biomass.rest;

import cl.camanchaca.biomass.adapter.bigquery.biomass.ProjectedBiomassBQDataAdapter;
import cl.camanchaca.biomass.validations.ValidationProjectedBiomass;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.responses.ProjectedBiomassResponse;
import cl.camanchaca.business.usecases.largoplazo.biomass.*;
import cl.camanchaca.domain.dtos.biomass.ProjectedBiomassDTO;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.utils.InputStreamUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class ProjectedBiomassController {

    private final MainErrorhandler errorhandler;

    private final String URL_BASE = "/biomass/projected";

    //
    @Bean
    public RouterFunction<ServerResponse> getAllListScenarios(GetAllBiomassScenariosUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE+"/scenario"),
                request -> ValidationProjectedBiomass
                        .validateParamsPagination(request, RequestParams.builder().build())
                        .flatMap(o -> {
                            ValidationProjectedBiomass.validateHeader(request.headers());
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
    public RouterFunction<ServerResponse> getAllByScenario(GetAllProjectBiomassScenarioUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE+"/scenario/detail"),
                request -> ValidationProjectedBiomass
                        .validateParams(request, RequestParams.builder().build())
                        .flatMap(o -> {
                            ValidationProjectedBiomass.validateHeader(request.headers());
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
    public RouterFunction<ServerResponse> generatedProjectedBiomass(GeneratedProjectedBiomassUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .POST(URL_BASE + "/generated"),
                request -> ValidationProjectedBiomass
                        .validateProjectedBiomass(request.bodyToFlux(ProjectedBiomassDTO.class))
                        .collectList()
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
    public RouterFunction<ServerResponse> getProjectedBiomassBySpecie(GetProjectBiomassBySpecieUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE + "/{specie}"),
                request -> Mono.fromCallable(() ->
                                request.pathVariable("specie"))
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
    public RouterFunction<ServerResponse> downloadProjectedBiomassBySpecie(GeneratedExcelProjectedBiomassUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE + "/excel/{specie}"),
                request -> Mono.fromCallable(() ->
                                request.pathVariable("specie"))
                        .flatMap(useCase::apply)
                        .flatMap(data -> {
                                    HttpHeaders headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                                    headers.setContentDispositionFormData("attachment", "biomasa_proyectada.xlsx");
                                    return ServerResponse.ok()
                                            .headers(h -> h.putAll(headers))
                                            .bodyValue(data);
                                }
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(errorhandler::badRequest)
        );
    }


    @Bean
    public RouterFunction<ServerResponse> uploadSize(ReadProjectedBiomassExcelUseCase useCase) {
        return route(RequestPredicates
                        .POST(URL_BASE + "/excel/{specie}")
                        .and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA)),
                request -> request.body(BodyExtractors.toMultipartData())
                        .flatMapMany(parts -> parts.toSingleValueMap()
                                .get("file")
                                .content())
                        .map(DataBuffer::asInputStream)
                        .collectList()
                        .flatMapMany(inputStreams -> {
                            InputStream combined = InputStreamUtils.combineInputStreams(inputStreams);
                            String specie = request.pathVariable("specie");
                            return useCase.apply(specie, combined);
                        })
                        .collectList()
                        .flatMap(s ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(s)
                        )
                        .onErrorResume(throwable ->
                                errorhandler.badRequest((Throwable) throwable)
                        )
        );
    }

    @Bean
    public RouterFunction<ServerResponse> saveProjectedBiomassBySpecie(SaveProjectedBiomassUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .POST(URL_BASE + "/{specie}"),
                request -> Mono.fromCallable(() ->
                                request.pathVariable("specie"))
                        .flatMapMany(specie ->
                                useCase
                                        .apply(
                                                request.bodyToFlux(ProjectedBiomassResponse.class),
                                                specie)
                        )
                        .collectList()
                        .flatMap(r -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(r)
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(errorhandler::badRequest)
        );
    }

}
