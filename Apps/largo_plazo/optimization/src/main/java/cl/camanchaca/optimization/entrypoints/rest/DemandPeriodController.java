package cl.camanchaca.optimization.entrypoints.rest;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.usecases.optimization.CreateDemandPeriodUseCase;
import cl.camanchaca.business.usecases.optimization.DownloadDemandPeriodExcelUseCase;
import cl.camanchaca.business.usecases.optimization.GetAllDemandPeriodUseCase;
import cl.camanchaca.business.usecases.shared.ReadExcelV2;
import cl.camanchaca.domain.models.optimization.DemandPeriod;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.optimization.validation.OptimizationValidations;
import cl.camanchaca.utils.InputStreamUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
import reactor.core.publisher.Flux;

import java.io.InputStream;
import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class DemandPeriodController {
    private final MainErrorhandler errorhandler;
    private static final String URL_BASE = "/optimization/demand";

    @Bean
    public RouterFunction<ServerResponse> getAll(GetAllDemandPeriodUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE),
                request -> OptimizationValidations
                        .validateGetAll(request, RequestParams.builder().build())
                        .flatMap(o -> {
                            OptimizationValidations.validateHeader(request.headers());
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
    public RouterFunction<ServerResponse> saveDemandPeriod(CreateDemandPeriodUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.PUT(URL_BASE).and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> request.bodyToFlux(DemandPeriod.class)
                        .collectList()
                        .flatMapMany(demandPeriods -> Flux.fromIterable(demandPeriods))
                        .transform(demandPeriodFlux -> OptimizationValidations.validateDemandPeriod(demandPeriodFlux))
                        .collectList()
                        .flatMapMany(demandPeriods -> useCase.apply(demandPeriods))
                        .collectList()
                        .flatMap(result -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(result))
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(error -> ServerResponse.badRequest().build())
        );
    }


    @Bean
    public RouterFunction<ServerResponse> bulkExcelDemandOffice(@Qualifier("getDemandPeriodExcel") ReadExcelV2<DemandPeriod> useCase) {
        return route(RequestPredicates
                        .POST(URL_BASE)
                        .and(RequestPredicates.accept(MediaType.MULTIPART_FORM_DATA)),
                request -> {
                    OptimizationValidations.validateHeader(request.headers());
                    String user = request.headers().header(Constans.USER.getValue()).get(0);
                    String office = request.headers().header(Constans.OFFICE.getValue()).get(0);
                    Map<String, String> headerInfo = Map.of(Constans.USER.getValue(), user, Constans.OFFICE.getValue(), office);

                    return request.body(BodyExtractors.toMultipartData())
                            .flatMapMany(parts -> parts.toSingleValueMap()
                                    .get("file")
                                    .content())
                            .map(DataBuffer::asInputStream)
                            .collectList()
                            .flatMapMany(inputStreams -> {
                                InputStream combined = InputStreamUtils.combineInputStreams(inputStreams);
                                Map<String, Object> values = Map.of("excel", combined, "headers", headerInfo);
                                return useCase.apply(values);
                            })
                            .collectList()
                            .flatMap(s ->
                                    ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(s)
                            )
                            .onErrorResume(errorhandler::badRequest);
                });
    }


    @Bean
    public RouterFunction<ServerResponse> downloadDemandPeriod(DownloadDemandPeriodExcelUseCase useCase) {
        return route(
                RequestPredicates.GET(URL_BASE + "/download"),
                request -> {
                    OptimizationValidations.validateHeader(request.headers());
                    String user = request.headers().header(Constans.USER.getValue()).get(0);
                    String office = request.headers().header(Constans.OFFICE.getValue()).get(0);
                    Map<String, String> headerInfo = Map.of(Constans.USER.getValue(), user, Constans.OFFICE.getValue(), office);

                    return useCase.apply(headerInfo)
                            .flatMap(excelBytes -> {
                                HttpHeaders headers = new HttpHeaders();
                                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=demand_periods.xlsx");
                                return ServerResponse
                                        .ok()
                                        .headers(httpHeaders -> httpHeaders.putAll(headers))
                                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                        .bodyValue(excelBytes);
                            })
                            .onErrorResume(errorhandler::badRequest)
                            .switchIfEmpty(ServerResponse.notFound().build());
                }
        );
    }




}
