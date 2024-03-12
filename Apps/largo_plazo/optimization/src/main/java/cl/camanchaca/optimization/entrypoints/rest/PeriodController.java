package cl.camanchaca.optimization.entrypoints.rest;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.usecases.optimization.GetSelectedPeriodUseCase;
import cl.camanchaca.business.usecases.optimization.GetVersionByPeriodSelectedUseCase;
import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.business.usecases.shared.ReadExcelV2;
import cl.camanchaca.domain.models.demand.UnrestrictedDemandOfficeExcel;
import cl.camanchaca.domain.models.optimization.DemandPeriod;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.optimization.validation.OptimizationValidations;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class PeriodController {
    public static final String BASE_URL = "/optimization/period";

    private final MainErrorhandler errorhandler;
    @Bean
    public RouterFunction<ServerResponse> getPeriodForThisUser(GetSelectedPeriodUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.GET(BASE_URL),
                request -> {
                    OptimizationValidations.validateHeader(request.headers());
                    String user = request.headers().header(Constans.USER.getValue()).get(0);
                    String office = request.headers().header(Constans.OFFICE.getValue()).get(0);
                    Map<String, String> headerInfo = Map.of(Constans.USER.getValue(), user, Constans.OFFICE.getValue(), office);

                    return useCase.apply(headerInfo)
                            .flatMap(parametersResponse -> ServerResponse
                                    .ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(parametersResponse)
                            )
                            .onErrorResume(errorhandler::badRequest);
                });
    }

    @Bean
    public RouterFunction<ServerResponse> getVersionForThisPeriod(GetVersionByPeriodSelectedUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates.GET(BASE_URL + "/version"),
                request -> {
                    OptimizationValidations.validateHeader(request.headers());
                    String user = request.headers().header(Constans.USER.getValue()).get(0);
                    String office = request.headers().header(Constans.OFFICE.getValue()).get(0);
                    Map<String, String> headerInfo = Map.of(Constans.USER.getValue(), user, Constans.OFFICE.getValue(), office);

                    return useCase.apply(headerInfo)
                            .flatMap(parametersResponse -> ServerResponse
                                    .ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(parametersResponse)
                            )
                            .onErrorResume(errorhandler::badRequest);
                });
    }


}
