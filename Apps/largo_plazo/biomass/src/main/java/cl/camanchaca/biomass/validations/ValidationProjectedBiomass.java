package cl.camanchaca.biomass.validations;

import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.domain.dtos.biomass.ProjectedBiomassDTO;
import cl.camanchaca.generics.errors.InfrastructureError;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class ValidationProjectedBiomass {
    public static Flux<ProjectedBiomassDTO> validateProjectedBiomass(Flux<ProjectedBiomassDTO> projectedBiomassDTOFlux) {
        return projectedBiomassDTOFlux.map(projectedBiomassDTO -> {
                    Stream.of(
                            projectedBiomassDTO.getNumber(),
                            projectedBiomassDTO.getKilosWFE(),
                            projectedBiomassDTO.getAvgWeight(),
                            projectedBiomassDTO.getDay(),
                            projectedBiomassDTO.getStandardDeviation(),
                            projectedBiomassDTO.getQuality(),
                            projectedBiomassDTO.getSpecie()
                    ).forEach(Objects::requireNonNull);
                    return projectedBiomassDTO;
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }
    private ValidationProjectedBiomass(){}

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

    public static Mono<RequestParams> validateParams(ServerRequest request, RequestParams params) {
        return Mono.fromCallable(() -> request.queryParam("page")
                        .get())
                .map(page -> {
                    int pageInt = Integer.parseInt(page);
                    int pageSize = Integer.parseInt(request.queryParam("pageSize").get());
                    String scenario = String.valueOf(request.queryParam("scenario").get());
                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new RuntimeException("La pagina o el tamaño no puede ser menor a 0");
                    }
                    if(scenario == null || scenario.isEmpty()){
                        throw new RuntimeException("Scenario no puede ser nulo");
                    }

                    String specie = String.valueOf(request.queryParam("specie").get());

                    if(specie == null || specie.isEmpty()){
                        throw new RuntimeException("Specie no puede ser nulo");
                    }

                    return params.toBuilder()
                            .page(pageInt)
                            .size(pageSize)
                            .selectedScenario(scenario)
                            .specie(specie)
                            .build();
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("02")));
    }

    public static void validateHeader(ServerRequest.Headers headers) {
        Stream.of(
                headers.header("user").get(0),
                headers.header("office").get(0)
        ).forEach(Objects::requireNonNull);
    }
}
