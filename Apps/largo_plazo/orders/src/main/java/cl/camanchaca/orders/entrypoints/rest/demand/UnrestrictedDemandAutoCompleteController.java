package cl.camanchaca.orders.entrypoints.rest.demand;

import cl.camanchaca.business.usecases.largoplazo.orders.GetSaleStatusUseCase;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.GetAllDestinationPortUseCase;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.GetAllIncoTermUseCase;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.GetOfficeUseCase;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.GetSalesTypeUseCase;
import cl.camanchaca.generics.MainErrorhandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@AllArgsConstructor
public class UnrestrictedDemandAutoCompleteController {

    private final MainErrorhandler errorhandler;

    private static final String URL_BASE = "/demand/autocomplete";

    @Bean
    public RouterFunction<ServerResponse> getOffice(GetOfficeUseCase useCase){
        return RouterFunctions.route(
                RequestPredicates.GET(URL_BASE +"/office"),
                request -> useCase.get()
                        .collectList()
                        .flatMap(s ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getSalesType(GetSalesTypeUseCase useCase){
        return RouterFunctions.route(
                RequestPredicates.GET(URL_BASE +"/sales-type"),
                request -> useCase.get()
                        .collectList()
                        .flatMap(s ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getAllSaleStatus(GetSaleStatusUseCase useCase){
        return RouterFunctions.route(
                RequestPredicates.GET(URL_BASE +"/sales-status"),
                request -> useCase.get()
                        .collectList()
                        .flatMap(s ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getAllIncoTerm(GetAllIncoTermUseCase useCase){
        return RouterFunctions.route(
                RequestPredicates.GET(URL_BASE +"/incoterm"),
                request -> useCase.get()
                        .collectList()
                        .flatMap(s ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getAllDestinatioPort(GetAllDestinationPortUseCase useCase){
        return RouterFunctions.route(
                RequestPredicates.GET(URL_BASE +"/ports"),
                request -> useCase.get()
                        .collectList()
                        .flatMap(s ->
                                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }

}
