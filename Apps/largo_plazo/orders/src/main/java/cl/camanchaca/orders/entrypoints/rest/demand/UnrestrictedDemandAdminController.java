package cl.camanchaca.orders.entrypoints.rest.demand;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.DownloadDemandUnrestrictedAdminUseCase;
import cl.camanchaca.business.usecases.largoplazo.orders.demand.GetAllUnrestrictedDemandAdminUseCase;
import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.orders.utils.InputStreamUtils;
import cl.camanchaca.orders.validations.DemandValidations;
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
import java.util.Map;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class UnrestrictedDemandAdminController {

    private final MainErrorhandler errorhandler;

    private static final String URL_BASE = "/demand/admin";
    @Bean
    public RouterFunction<ServerResponse> getAllAdmin(GetAllUnrestrictedDemandAdminUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE),
                request -> DemandValidations
                        .validateGetAll(request, RequestParams.builder().build())
                        .flatMap(o -> {
                            DemandValidations.validateHeader(request.headers());
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
    public RouterFunction<ServerResponse> downloadInfoAdmin(DownloadDemandUnrestrictedAdminUseCase useCase) {
        return route(
                RequestPredicates.GET(URL_BASE + "/download"),
                request -> DemandValidations
                        .validateParamsPagination(request, RequestParams.builder().build())
                        .flatMap(o -> {
                            DemandValidations.validateHeader(request.headers());
                            String user = request.headers().header(Constans.USER.getValue()).get(0);
                            String office = request.headers().header(Constans.OFFICE.getValue()).get(0);
                            Map<String, String> headerInfo = Map.of(Constans.USER.getValue(), user, Constans.OFFICE.getValue(), office);
                            return useCase.apply(o, headerInfo)
                                    .flatMap(excelBytes -> {
                                        HttpHeaders headers = new HttpHeaders();
                                        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=demand_admin.xlsx");
                                        return ServerResponse
                                                .ok()
                                                .headers(httpHeaders -> httpHeaders.putAll(headers))
                                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                                .bodyValue(excelBytes);
                                    });
                        })
                        .onErrorResume(errorhandler::badRequest)
                        .switchIfEmpty(ServerResponse.notFound().build())
        );
    }


    @Bean
    public RouterFunction<ServerResponse> bulkFileDemand(@Qualifier("getReadDemandAdminExcel") ReadExcel<UnrestrictedDemand> useCase) {
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



}
