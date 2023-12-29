package cl.camanchaca.parametrization.validations;

import cl.camanchaca.domain.dtos.ParameterSizePerformanceDTO;
import cl.camanchaca.generics.errors.InfrastructureError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.stream.Stream;

public class ValidationsSizePerformance {

    public static Flux<ParameterSizePerformanceDTO> validateBodySize(Flux<ParameterSizePerformanceDTO> body) {
        return body.map(dto -> {
                    Stream.of(
                            dto.getSizeId(),
                            dto.getProductId(),
                            dto.getStatus()
                    ).forEach(Objects::requireNonNull);
                    return dto;
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }

    public static Flux<ParameterSizePerformanceDTO> validateBodyPerformance(Flux<ParameterSizePerformanceDTO> body) {
        return body.map(dto -> {
                    Stream.of(
                            dto.getSizeId(),
                            dto.getProductId(),
                            dto.getPerformance()
                    ).forEach(Objects::requireNonNull);
                    return dto;
                })
                .onErrorResume(throwable -> Mono.error(new InfrastructureError("03")));
    }

    private ValidationsSizePerformance() {
    }
}
