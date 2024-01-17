package cl.camanchaca.biomass.validations;

import cl.camanchaca.business.generic.Constans;
import cl.camanchaca.business.generic.RequestParams;
import cl.camanchaca.domain.dtos.biomass.ProjectedBiomassDTO;
import cl.camanchaca.generics.errors.InfraestructureException;
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
                    int pageSize = Integer.parseInt(request.queryParam(Constans.PAGE_SIZE.getValue()).get());
                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new InfraestructureException(Constans.PAGE_ERROR.getValue());
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
                    int pageSize = Integer.parseInt(request.queryParam(Constans.PAGE_SIZE.getValue()).get());
                    String scenario = String.valueOf(request.queryParam("scenario").get());
                    if (pageInt <= 0 || pageSize <= 0) {
                        throw new InfraestructureException(Constans.PAGE_ERROR.getValue());
                    }
                    if(scenario == null || scenario.isEmpty()){
                        throw new InfraestructureException("Scenario no puede ser nulo");
                    }

                    String specie = String.valueOf(request.queryParam(Constans.SPECIE.getValue()).get());

                    if(specie == null || specie.isEmpty()){
                        throw new InfraestructureException("Specie no puede ser nulo");
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
                headers.header(Constans.USER.getValue()).get(0),
                headers.header(Constans.OFFICE.getValue()).get(0)
        ).forEach(Objects::requireNonNull);
    }
}
