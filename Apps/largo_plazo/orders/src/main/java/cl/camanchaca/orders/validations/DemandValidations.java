package cl.camanchaca.orders.validations;

import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.domain.dtos.*;
import cl.camanchaca.domain.models.demand.UnrestrictedDemand;
import cl.camanchaca.generics.errors.InfrastructureError;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class DemandValidations {

    public DemandValidations() {
    }

    public static Mono<RequestParams> validateGetAll(ServerRequest request, RequestParams params) {
        return Mono.fromCallable(() -> request.queryParam("page")
                        .get())
                .map(page -> {
                    int pageInt = Integer.parseInt(page);
                    int pageSize = Integer.parseInt(request.queryParam("pageSize").get());

                    String specie = request.queryParam("specie").orElse(null);
                    String family = request.queryParam("family").orElse(null);


                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new RuntimeException("La pagina o el tamaño no puede ser menor a 0");
                    }

                    return params.toBuilder()
                            .page(pageInt)
                            .size(pageSize)
                            .specie(specie)
                            .family(family)
                            .build();
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }

    public static Mono<RequestParams> validateSummary(ServerRequest request, RequestParams params) {
        return Mono.fromCallable(() -> request.queryParam("page")
                        .get())
                .map(page -> {
                    int pageInt = Integer.parseInt(page);
                    int pageSize = Integer.parseInt(request.queryParam("pageSize").get());

                    String specie = request.queryParam("specie").get();
                    
                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new RuntimeException("La pagina o el tamaño no puede ser menor a 0");
                    }

                    if(specie == null){
                        specie = "";
                    }

                    return params.toBuilder()
                            .page(pageInt)
                            .size(pageSize)
                            .specie(specie)
                            .build();
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }

    public static Mono<RequestParams> validateParamsPagination(ServerRequest request, RequestParams params) {
        return Mono.fromCallable(() -> request.queryParam("page")
                        .get())
                .map(page -> {
                    int pageInt = Integer.parseInt(page);
                    int pageSize = Integer.parseInt(request.queryParam("pageSize").get());
                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new RuntimeException("La pagina o el tamaño no puede ser menor a 0");
                    }
                    return params.toBuilder()
                            .page(pageInt)
                            .size(pageSize)
                            .build();
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }

    public static Mono<FreightRequestDto> validateFreight(Mono<FreightRequestDto> body) {
        return body
                .doOnNext(dto -> {
                    Objects.requireNonNull(dto.getIncoterms());
                    Objects.requireNonNull(dto.getPort());
                });
    }

    public static Mono<InsuranceRequestDto> validateInsurance(Mono<InsuranceRequestDto> body) {
        return body
                .doOnNext(dto -> {
                    Objects.requireNonNull(dto.getIncoterm());
                    Objects.requireNonNull(dto.getUnit());
                });
    }


    public static Mono<GenerateFobRequestDto> validateGenerateFob(Mono<GenerateFobRequestDto> body) {
        return body
                .doOnNext(dto -> {
                    Objects.requireNonNull(dto.getPurchPrice());
                    Objects.requireNonNull(dto.getIncoterm());
                    Objects.requireNonNull(dto.getInsurance());
                    Objects.requireNonNull(dto.getFreight());
                });
    }

    public static Mono<GenerateRmpRequestDto> validateRmp(Mono<GenerateRmpRequestDto> body) {
        return body
                .doOnNext(dto -> {
                    Objects.requireNonNull(dto.getCostEmpt());
                    Objects.requireNonNull(dto.getCostDist());
                    Objects.requireNonNull(dto.getCostProc());
                    Objects.requireNonNull(dto.getAporteNeto());
                    Objects.requireNonNull(dto.getFob());
                });
    }

    public static Mono<GenerateKgWFEDemandRequestDto> validateKgWFEDemand(Mono<GenerateKgWFEDemandRequestDto> body) {
        return body
                .doOnNext(dto -> {
                    Objects.requireNonNull(dto.getRend());
                    Objects.requireNonNull(dto.getKilosNetos());
                });
    }



    public static Flux<UnrestrictedDemand> validateRequestBody(Flux<UnrestrictedDemand> body) {
        return body
                .<UnrestrictedDemand>handle((dto, sink) -> {
                    try {
                        Stream.of(
                                dto.getUsuario(),
                                dto.getPeriodo(),
                                dto.getOfficeSalesType(),
                                dto.getOficina()
                        ).forEach(Objects::requireNonNull);
                        sink.next(dto);
                    } catch (Exception e) {
                        // Agregar información de registro aquí
                        System.out.println("Error de validación en validateRequestBody" + e);
                        sink.error(e);
                    }
                })
                .onErrorResume(throwable -> Mono.error(new RuntimeException(throwable.getLocalizedMessage())));
    }

    public static void validateHeader(ServerRequest.Headers headers) {
        Stream.of(
                headers.header("user").get(0),
                headers.header("office").get(0)
        ).forEach(Objects::requireNonNull);

    }
}
