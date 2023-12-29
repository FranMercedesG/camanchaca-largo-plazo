package cl.camanchaca.parametrization.entrypoints.rest;

import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.usecases.largoplazo.parameters.GetParameterMinimunAsignationByCodeUseCase;
import cl.camanchaca.business.usecases.largoplazo.parameters.GetParameterMinimunAsignationUseCase;
import cl.camanchaca.business.usecases.largoplazo.parameters.SaveParameterGroupUseCase;
import cl.camanchaca.business.usecases.largoplazo.parameters.SaveParameterMinimumUseCase;
import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.domain.dtos.ParameterGroupDTO;
import cl.camanchaca.domain.dtos.ParameterMinimumDTO;
import cl.camanchaca.domain.models.product.ProductGroup;
import cl.camanchaca.domain.models.product.ProductMinimum;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.parametrization.utils.InputStreamUtils;
import cl.camanchaca.parametrization.validations.ParametersValidations;
import cl.camanchaca.parametrization.validations.ValidationsGroup;
import cl.camanchaca.parametrization.validations.ValidationsMinimum;
import lombok.AllArgsConstructor;
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
import org.xml.sax.ErrorHandler;
import reactor.core.publisher.Mono;

import java.io.InputStream;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class MinimumController {

    private final MainErrorhandler errorhandler;

    private final String URL_BASE = "/parameters/minimum";

    @Bean
    public RouterFunction<ServerResponse> getMinimunAsignation(GetParameterMinimunAsignationUseCase useCase) {
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
    public RouterFunction<ServerResponse> getMinimunAsignationByCode(GetParameterMinimunAsignationByCodeUseCase useCase) {
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
    public RouterFunction<ServerResponse> uploadMinimun(@Qualifier("getReadMinimumExcel") ReadExcel<ProductMinimum> useCase) {
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
                        .onErrorResume(throwable ->
                                errorhandler.badRequest(throwable)
                        )
        );
    }

    @Bean
    public RouterFunction<ServerResponse> saveMinimum(SaveParameterMinimumUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.PUT(URL_BASE)
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> ValidationsMinimum.validateBodyMinimum(
                                request.bodyToFlux(ParameterMinimumDTO.class)
                        )
                        .collectList()
                        .flatMapMany(useCase::apply)
                        .collectList()
                        .flatMap(s ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(s)
                        )
                        .onErrorResume(throwable ->
                                errorhandler.badRequest(throwable)
                        )
        );
    }
}
