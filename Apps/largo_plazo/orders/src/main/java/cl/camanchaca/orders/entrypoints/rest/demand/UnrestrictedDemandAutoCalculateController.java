package cl.camanchaca.orders.entrypoints.rest.demand;

import cl.camanchaca.business.usecases.largoplazo.orders.demand.*;
import cl.camanchaca.domain.dtos.*;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.orders.validations.DemandValidations;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@AllArgsConstructor
public class UnrestrictedDemandAutoCalculateController {

    private final MainErrorhandler errorhandler;

    private final String URL_BASE = "/demand/autocalculate";

    @Bean
    public RouterFunction<ServerResponse> calculateFreight(GetAutocalculateFreightUseCase useCase) {
        return route(
                RequestPredicates
                        .POST(URL_BASE + "/freight")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> DemandValidations.validateFreight(request.bodyToMono(FreightRequestDto.class))
                        .flatMapMany(o -> {
                            DemandValidations.validateHeader(request.headers());
                            String user = request.headers().header("user").get(0);
                            String office = request.headers().header("office").get(0);
                            Map<String, String> headerInfo = Map.of("user", user, "office", office);
                            try {
                                return useCase.apply(o, headerInfo);
                            } catch (InterruptedException e) {
                                throw new RuntimeException("Ha ocurrido un error entiendo obteniendo los datos");
                            }
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
    public RouterFunction<ServerResponse> calculateInsurance(GetAutocalculateInsuranceUseCase useCase) {
        return route(
                RequestPredicates
                        .POST(URL_BASE + "/insurance")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> DemandValidations.validateInsurance(request.bodyToMono(InsuranceRequestDto.class))
                        .flatMapMany(o -> {
                            DemandValidations.validateHeader(request.headers());
                            String user = request.headers().header("user").get(0);
                            String office = request.headers().header("office").get(0);
                            Map<String, String> headerInfo = Map.of("user", user, "office", office);
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
    public RouterFunction<ServerResponse> calculateFob(GetAutocalculateFobUseCase useCase) {
        return route(
                RequestPredicates
                        .POST(URL_BASE + "/fob")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> DemandValidations.validateGenerateFob(request.bodyToMono(GenerateFobRequestDto.class))
                        .flatMapMany(o -> {
                            DemandValidations.validateHeader(request.headers());
                            String user = request.headers().header("user").get(0);
                            String office = request.headers().header("office").get(0);
                            Map<String, String> headerInfo = Map.of("user", user, "office", office);
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
    public RouterFunction<ServerResponse> calculateRmp(GetAutoCalculateRmpUseCase useCase) {
        return route(
                RequestPredicates
                        .POST(URL_BASE + "/rmp")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> DemandValidations.validateRmp(request.bodyToMono(GenerateRmpRequestDto.class))
                        .flatMapMany(o -> {
                            DemandValidations.validateHeader(request.headers());
                            String user = request.headers().header("user").get(0);
                            String office = request.headers().header("office").get(0);
                            Map<String, String> headerInfo = Map.of("user", user, "office", office);
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
    public RouterFunction<ServerResponse> calculateKgWFEDemand(GetAutoCalculateKgWFEDemandUseCase useCase) {
        return route(
                RequestPredicates
                        .POST(URL_BASE + "/kgwfe-demand")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> DemandValidations.validateKgWFEDemand(request.bodyToMono(GenerateKgWFEDemandRequestDto.class))
                        .flatMapMany(o -> {
                            DemandValidations.validateHeader(request.headers());
                            String user = request.headers().header("user").get(0);
                            String office = request.headers().header("office").get(0);
                            Map<String, String> headerInfo = Map.of("user", user, "office", office);
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
}




