package cl.camanchaca.capacity.adapters.entrypoints.rest.capacity.minimum;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.usecases.largoplazo.capacity.minimum.*;
import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.capacity.utils.InputStreamUtils;
import cl.camanchaca.capacity.validations.CapacityValidations;
import cl.camanchaca.domain.models.capacity.minimum.MinimumCapacity;
import cl.camanchaca.generics.MainErrorhandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.io.InputStream;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class MinimumCapacityController {

    private final MainErrorhandler errorhandler;
    private static final String URL_BASE = "/capacity/minimum";

    @Bean
    public RouterFunction<ServerResponse> getAllMinimumCapacityDaily(GetAllMinimumProductiveCapacityUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE + "/daily"),
                request -> CapacityValidations
                        .validateParamsPagination(request, RequestParams.builder().build())
                        .flatMap(o -> {
                            CapacityValidations.validateHeader(request.headers());
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
    public RouterFunction<ServerResponse> generateMinimumTotal(GetAllTotalMinimumProductiveCapacityUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE + "/daily/total"),
                request -> CapacityValidations
                        .validateParamsPagination(request, RequestParams.builder().build())
                        .flatMap(o -> {
                            CapacityValidations.validateHeader(request.headers());
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
    public RouterFunction<ServerResponse> saveMinimum(SaveAllMinimumProductiveCapacityUseCase useCase) {
        return route(
                RequestPredicates
                        .PUT(URL_BASE + "/daily")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> CapacityValidations.validateMinimumCapacityRequestBody(request.bodyToFlux(MinimumCapacity.class))
                        .collectList()
                        .flatMapMany(o -> {
                            CapacityValidations.validateHeader(request.headers());
                            String user = request.headers().header(Constans.USER.getValue()).get(0);
                            String office = request.headers().header(Constans.OFFICE.getValue()).get(0);
                            Map<String, String> headerInfo = Map.of(Constans.USER.getValue(), user, Constans.OFFICE.getValue(), office);
                            return useCase.apply(o, headerInfo);
                        })
                        .collectList()
                        .flatMap(s ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> saveMinimumTotal(SaveAllTotalMinimumProductiveCapacityUseCase useCase) {
        return route(
                RequestPredicates
                        .PUT(URL_BASE + "/daily/total")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> CapacityValidations.validateMinimumCapacityRequestBody(request.bodyToFlux(MinimumCapacity.class))
                        .collectList()
                        .flatMapMany(o -> {
                            CapacityValidations.validateHeader(request.headers());
                            String user = request.headers().header(Constans.USER.getValue()).get(0);
                            String office = request.headers().header(Constans.OFFICE.getValue()).get(0);
                            Map<String, String> headerInfo = Map.of(Constans.USER.getValue(), user, Constans.OFFICE.getValue(), office);
                            return useCase.apply(o, headerInfo);
                        })
                        .collectList()
                        .flatMap(s ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getTotalMinimumSaved(GetAllTotalMinimumSavedUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE + "/total"),
                request -> CapacityValidations
                        .validateParamsPagination(request, RequestParams.builder().build())
                        .flatMap(o -> {
                            CapacityValidations.validateHeader(request.headers());
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
    public RouterFunction<ServerResponse> bulkExcelMinimum(@Qualifier("getCapacityMinimumTotalBulkExcel") ReadExcel<MinimumCapacity> useCase) {
        return route(RequestPredicates
                        .POST(URL_BASE + "/total")
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
