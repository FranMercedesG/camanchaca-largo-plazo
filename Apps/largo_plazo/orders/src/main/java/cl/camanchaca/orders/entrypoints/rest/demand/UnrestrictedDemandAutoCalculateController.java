package cl.camanchaca.orders.entrypoints.rest.demand;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.*;
import cl.camanchaca.domain.dtos.*;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.generics.errors.InfraestructureException;
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

    private static final String URL_BASE = "/demand/autocalculate";

    @Bean
    public RouterFunction<ServerResponse> calculateFreight(GetAutocalculateFreightUseCase useCase) {
        return route(
                RequestPredicates
                        .POST(URL_BASE + "/freight")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> DemandValidations.validateFreight(request.bodyToMono(FreightRequestDto.class))
                        .flatMapMany(o -> {
                            DemandValidations.validateHeader(request.headers());
                            String user = request.headers().header(Constans.USER.getValue()).get(0);
                            String office = request.headers().header(Constans.OFFICE.getValue()).get(0);
                            Map<String, String> headerInfo = Map.of(Constans.USER.getValue(), user, Constans.OFFICE.getValue(), office);
                            try {
                                return useCase.apply(o, headerInfo);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                                throw new InfraestructureException("Ha ocurrido un error entiendo obteniendo los datos");
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
    public RouterFunction<ServerResponse> calculateFob(GetAutocalculateFobUseCase useCase) {
        return route(
                RequestPredicates
                        .POST(URL_BASE + "/fob")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> DemandValidations.validateGenerateFob(request.bodyToMono(GenerateFobRequestDto.class))
                        .flatMapMany(o -> {
                            DemandValidations.validateHeader(request.headers());
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
    public RouterFunction<ServerResponse> calculateRmp(GetAutoCalculateRmpUseCase useCase) {
        return route(
                RequestPredicates
                        .POST(URL_BASE + "/rmp")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> DemandValidations.validateRmp(request.bodyToMono(GenerateRmpRequestDto.class))
                        .flatMapMany(o -> {
                            DemandValidations.validateHeader(request.headers());
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
    public RouterFunction<ServerResponse> calculateKgWFEDemand(GetAutoCalculateKgWFEDemandUseCase useCase) {
        return route(
                RequestPredicates
                        .POST(URL_BASE + "/kgwfe-demand")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> DemandValidations.validateKgWFEDemand(request.bodyToMono(GenerateKgWFEDemandRequestDto.class))
                        .flatMapMany(o -> {
                            DemandValidations.validateHeader(request.headers());
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
}




