package cl.camanchaca.biomass.rest;

import cl.camanchaca.biomass.validations.BiomassValidations;
import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.responses.GroupSizeMaxResponse;
import cl.camanchaca.business.usecases.largoplazo.biomass.GetGroupMaxBiomassBySpecieUseCase;
import cl.camanchaca.business.usecases.largoplazo.biomass.GetGroupUseCase;
import cl.camanchaca.business.usecases.largoplazo.biomass.SaveGroupMaxUseCase;
import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.domain.models.biomass.GroupSizeMaximum;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.utils.InputStreamUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.InputStream;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class GroupMaxController {

    private final MainErrorhandler errorhandler;

    private static final String URL_BASE = "/biomass/group-max";

    @Bean
    public RouterFunction<ServerResponse> getGroups(GetGroupUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE+ "/group"),
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
    public RouterFunction<ServerResponse> getAllGroup(GetGroupMaxBiomassBySpecieUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE),
                request -> BiomassValidations
                        .validateParams(request, RequestParams.builder().build())
                        .flatMap(o -> {
                            BiomassValidations.validateHeader(request.headers());
                            String user = request.headers().header(Constans.USER.getValue()).get(0);
                            String office = request.headers().header(Constans.OFFICE.getValue()).get(0);
                            Map<String, String> headerInfo = Map.of(Constans.USER.getValue(), user, Constans.OFFICE.getValue(), office);
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
    public RouterFunction<ServerResponse> saveGroupMax(SaveGroupMaxUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .PUT(URL_BASE)
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request ->
                        BiomassValidations
                                .validateSaveGroupMax(
                                        request.bodyToMono(GroupSizeMaxResponse.class)
                                )
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

    @Bean
    public RouterFunction<ServerResponse> bulkFileGroupSize(@Qualifier("getGroupMaxBulkExcel") ReadExcel<GroupSizeMaximum> useCase) {
        return route(RequestPredicates
                        .POST(URL_BASE + "/bulk")
                        .and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA)),
                request -> request.body(BodyExtractors.toMultipartData())
                        .flatMapMany(parts -> parts.toSingleValueMap()
                                .get("file")
                                .content())
                        .map(DataBuffer::asInputStream)
                        .collectList()
                        .flatMapMany(inputStreams -> {
                            InputStream combined = InputStreamUtils.combineInputStreams(inputStreams);
                            return useCase.apply(combined);
                        })
                        .collectList()
                        .flatMap(s ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
        );
    }

}
