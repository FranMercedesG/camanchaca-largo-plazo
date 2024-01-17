package cl.camanchaca.parametrization.entrypoints.rest;

import cl.camanchaca.business.usecases.largoplazo.parameters.*;
import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.domain.dtos.group.GroupSKUParameterDTO;
import cl.camanchaca.domain.models.product.ProductGroup;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.parametrization.utils.InputStreamUtils;
import cl.camanchaca.parametrization.validations.ValidationsGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.InputStream;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.parametrization.validations.ParametersValidations;
import org.springframework.web.reactive.function.server.RouterFunctions;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class GroupController {
    private final MainErrorhandler errorhandler;

    private static final String URL_BASE = "/parameters/group";

    @Bean
    public RouterFunction<ServerResponse> uploadGroup(@Qualifier("getReadGroupExcel") ReadExcel<ProductGroup> useCase) {
        return route(RequestPredicates
                        .POST(URL_BASE)
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
@Bean
    public RouterFunction<ServerResponse> getGroupAsignation(GetParameterGroupUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE),
                request -> ParametersValidations
                        .validateParamsPagination(request, RequestParams.builder().build())
                        .flatMap(useCase::apply)
                        .flatMap(s ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getGroupAsignationByCode(GetParameterGroupByCodeUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE + "/{code}"),
                request -> Mono.fromCallable(() ->
                                request.pathVariable("code"))
                        .map(Integer::valueOf)
                        .flatMap(useCase::apply)
                        .flatMap(s ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    @Bean
    public RouterFunction<ServerResponse> saveGroup(SaveParameterGroupUseCase useCase) {

        return RouterFunctions.route(
                RequestPredicates
                        .PUT(URL_BASE)
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request ->
                        ValidationsGroup
                                .validateBody(
                                        request.bodyToMono(GroupSKUParameterDTO.class)
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
        public RouterFunction<ServerResponse> downloadGroup(DownloadParameterGroupUseCase useCase) {
            return RouterFunctions.route(
                    RequestPredicates.GET(URL_BASE + "/excel/download"),
                    request -> useCase.apply()
                            .flatMap(data -> {
                                        HttpHeaders headers = new HttpHeaders();
                                        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                                        headers.setContentDispositionFormData("attachment", "group_parameter.xlsx");
                                        return ServerResponse.ok()
                                                .headers(h -> h.putAll(headers))
                                                .bodyValue(data);
                                    }
                            )
                            .switchIfEmpty(ServerResponse.notFound().build())
                            .onErrorResume(errorhandler::badRequest)
            );
        }



}
