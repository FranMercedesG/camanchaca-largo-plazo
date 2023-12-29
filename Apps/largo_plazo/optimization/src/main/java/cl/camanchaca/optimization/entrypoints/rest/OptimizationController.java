package cl.camanchaca.optimization.entrypoints.rest;

import cl.camanchaca.business.generic.UpdateOptimizationBodyRequest;
import cl.camanchaca.business.usecases.largoplazo.orders.SavePeriodsUseCase;
import cl.camanchaca.business.usecases.optimization.ActiveOptimizationUseCase;
import cl.camanchaca.business.usecases.optimization.DownloadActiveExcelUseCase;
import cl.camanchaca.business.usecases.optimization.GetActiveOptimizationByMonthUseCase;
import cl.camanchaca.business.usecases.optimization.GetOptimizationByMonthAndVersionUseCase;
import cl.camanchaca.business.usecases.optimization.UpdateOptimizationUseCase;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.optimization.validation.OptimizationValidations;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class OptimizationController {
    private final MainErrorhandler errorhandler;

    private final String URL_BASE = "/optimization";


    @Bean
    public RouterFunction<ServerResponse> getActiveByMonth(GetActiveOptimizationByMonthUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.GET(URL_BASE + "/active"),
                request -> {
                    OptimizationValidations.validateHeader(request.headers());
                    String user = request.headers().header("user").get(0);
                    String office = request.headers().header("office").get(0);
                    Map<String, String> headerInfo = Map.of("user", user, "office", office);
                    return useCase.apply(headerInfo)
                            .collectList()
                            .flatMap(s ->
                                    ServerResponse
                                            .ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(s)
                            )
                            .onErrorResume(errorhandler::badRequest)
                            .switchIfEmpty(ServerResponse.notFound().build());

                }
        );
    }


    @Bean
    public RouterFunction<ServerResponse> downloadActive(DownloadActiveExcelUseCase useCase) {
        return route(
                RequestPredicates.POST(URL_BASE + "/active/download"),
                request -> {
                    OptimizationValidations.validateHeader(request.headers());
                    String user = request.headers().header("user").get(0);
                    String office = request.headers().header("office").get(0);
                    Map<String, String> headerInfo = Map.of("user", user, "office", office);

                    return useCase.apply(headerInfo)
                            .flatMap(excelBytes -> {
                                HttpHeaders headers = new HttpHeaders();
                                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=optimization_active.xlsx");
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


    @Bean
    public RouterFunction<ServerResponse> getOptimizationByMonthAndVersion(GetOptimizationByMonthAndVersionUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.GET(URL_BASE + "/version"),
                request -> OptimizationValidations.validateMonth(request)
                        .zipWith(OptimizationValidations.validateVersion(request))
                        .flatMapMany(tuple2 -> useCase
                                .apply(tuple2.getT1(), tuple2.getT2()))
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
    public RouterFunction<ServerResponse> setActiveOptimization(ActiveOptimizationUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.POST(URL_BASE + "/active"),
                request -> request.bodyToFlux(UUID.class)
                        .collectList()
                        .flatMap(useCase)
                        .flatMap(result ->
                                ServerResponse.accepted()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue("Se actualizaron: " + result + " datos")
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(errorhandler::badRequest)
        );
    }


    @Bean
    public RouterFunction<ServerResponse> saveActiveOptimization(UpdateOptimizationUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.POST(URL_BASE),
                request -> request.bodyToFlux(UpdateOptimizationBodyRequest.class)
                        .flatMap(useCase)
                        .count()
                        .flatMap(result ->
                                ServerResponse.accepted()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue("Se actualizaron: " + result + " datos")
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(errorhandler::badRequest)
        );
    }
}
