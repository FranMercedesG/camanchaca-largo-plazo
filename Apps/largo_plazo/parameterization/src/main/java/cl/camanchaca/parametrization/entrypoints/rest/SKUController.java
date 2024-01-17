package cl.camanchaca.parametrization.entrypoints.rest;

import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.business.usecases.largoplazo.parameters.DownloadParameterSKUUseCase;
import cl.camanchaca.business.usecases.largoplazo.parameters.GetParameterSKUByCodeUseCase;
import cl.camanchaca.business.usecases.largoplazo.parameters.GetParametersSKUUseCase;
import cl.camanchaca.business.usecases.largoplazo.parameters.SaveSKUUseCase;
import cl.camanchaca.business.usecases.shared.ReadExcel;
import cl.camanchaca.domain.models.Product;
import cl.camanchaca.generics.MainErrorhandler;
import cl.camanchaca.parametrization.utils.InputStreamUtils;
import cl.camanchaca.parametrization.validations.ParametersValidations;
import cl.camanchaca.parametrization.validations.ValidationProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import reactor.core.publisher.Mono;

import java.io.InputStream;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SKUController {

    private final MainErrorhandler errorhandler;

    private static final String URL_BASE = "/parameters/sku";

    @Bean
    public RouterFunction<ServerResponse> getProductByCode(GetParameterSKUByCodeUseCase useCase){
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE+"/{code}"),
                request -> Mono.fromCallable(()->
                        request.pathVariable("code"))
                        .map(Integer::valueOf)
                        .flatMap(useCase::apply)
                        .flatMap(s ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(s)
                        )
                        .switchIfEmpty(ServerResponse.notFound().build())
                        .onErrorResume(errorhandler::badRequest)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> getSKU(GetParametersSKUUseCase useCase) {
        return route(
                RequestPredicates
                        .GET(URL_BASE)
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> ParametersValidations
                        .validateParamsPagination(request, RequestParams.builder().build())
                        .flatMap(useCase)
                        .flatMap(s ->
                                ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
        );
    }


    @Bean
    public RouterFunction<ServerResponse> uploadSKU(@Qualifier("getReadSKUexcel") ReadExcel<Product> useCase) {
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
    public RouterFunction<ServerResponse> saveSKU(SaveSKUUseCase useCase) {
        return route(RequestPredicates
                        .PUT(URL_BASE)
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)),
                request -> ValidationProduct
                        .validateProduct(request.bodyToFlux(Product.class))
                        .collectList()
                        .flatMapMany(useCase::apply)
                        .collectList()
                        .flatMap(s -> ServerResponse
                                .ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(s)
                        )
                        .onErrorResume(errorhandler::badRequest)
        );
    }

    @Bean
    public RouterFunction<ServerResponse> downloadSkuExcel(DownloadParameterSKUUseCase useCase) {
        return RouterFunctions.route(
                RequestPredicates
                        .GET(URL_BASE + "/excel/download"),
                request -> useCase.get()
                        .flatMap(data -> {
                                    HttpHeaders headers = new HttpHeaders();
                                    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                                    headers.setContentDispositionFormData("attachment", "sku_parameter.xlsx");
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
